package app.domain.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Bitacora;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.CurrencyType;
import app.domain.models.enums.OperationBitacora;
import app.domain.models.enums.RolUser;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.BitacoraPort;
import app.domain.ports.TransferPort;
import app.domain.ports.UserPort;

@Service
public class CreateTransfer {
    private final TransferPort transferPort;
    private final ExecuteTransfer executeTransfer;
    private final UserPort userPort;
    private final BankAccountPort bankAccountPort;
    private final BitacoraPort bitacoraPort;

    @Autowired
    public CreateTransfer(TransferPort transferPort, UserPort userPort, ExecuteTransfer executeTransfer, BankAccountPort bankAccountPort, BitacoraPort bitacoraPort) {
        this.transferPort = transferPort;
        this.userPort = userPort;
        this.executeTransfer = executeTransfer;
        this.bankAccountPort = bankAccountPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void createTransfer(Transfer transfer, User user) throws BussinesException {
        BankAccount origenAccount = transfer.getOriginAccount();
        BankAccount destinationAccount = transfer.getDestinationAccount();

        // Validar que el usuario exista
        if (user == null) {
            throw new BussinesException("Usuario no encontrado");
        }

        // Validamos que el id sea único
        if (transfer.getId() != null && transferPort.existsById(transfer.getId())) {
            throw new BussinesException("Ya existe una transferencia con el mismo id");
        }

        // Validamos que cuenta origen y cuenta destino existan
        if (origenAccount == null) {
            throw new BussinesException("No se ha encontrado la cuenta de origen");
        }

        if (destinationAccount == null) {
            throw new BussinesException("No se ha encontrado la cuenta de destino");
        }

        // Cargamos las cuentas completas desde la BD
        BankAccount origenAccountFull = bankAccountPort.findByAccountNumber(origenAccount.getAccountNumber());
        if (origenAccountFull == null) {
            throw new BussinesException("No se ha encontrado la cuenta de origen");
        }

        BankAccount destinationAccountFull = bankAccountPort.findByAccountNumber(destinationAccount.getAccountNumber());
        if (destinationAccountFull == null) {
            throw new BussinesException("No se ha encontrado la cuenta de destino");
        }

        //Validamos que no sea la misma cuenta
        if(origenAccountFull.getAccountNumber() == (destinationAccountFull.getAccountNumber())){
            throw new BussinesException("No es posible transferir a la misma cuenta");
        }

        //Validamos que quien este creando la transferencia sea dueño de la cuenta origen
        if(!origenAccountFull.getCustomerOwner().getDocument().equals(user.getDocument()) &&
        (user.getSystemRole().equals(RolUser.PersonCustomerUser) ||
            user.getSystemRole().equals(RolUser.CorporateCustomerUser))) {

            throw new BussinesException("No puedes crear esta transferencia, no eres dueño de la cuenta bancaria");
        }

        // Si es CorporateCustomer validamos que la cuenta si sea de la empresa
        if (user.getSystemRole().equals(RolUser.CorporateEmployee)) {
            if (!origenAccountFull.getCustomerOwner().getDocument()
                    .equals(user.getCustomer().getDocument())) {
                System.out.println(origenAccountFull.getCustomerOwner().getDocument());
                System.out.println(user.getCustomer().getDocument());

                throw new BussinesException(
                    "La empresa no es dueña de la cuenta bancaria"
                );
            }
        }

        // Validamos que el creador de la transferencia exista
        transfer.setIdCreator(user.getDocument());
        User createUser = userPort.findByDocument(transfer.getIdCreator());
        if (createUser == null) {
            throw new BussinesException("No se ha encontrado el usuario creador de la transferencia");
        }

        //Validamos que ambas cuantas tengan el mismo tipo de moneda
        if(!origenAccountFull.getCurrencyType().equals(destinationAccountFull.getCurrencyType())){
            throw new BussinesException("No es posible crear transferencia, las cuentas tienen diferente tipo de moneda");
        }

        //Validamos que el monto no este por debajo del minimo eprmitido
        if(transfer.getAmount().compareTo(getMinAmount(origenAccountFull.getCurrencyType())) < 0){
            throw new BussinesException("La transferencia esta por debajo del minimo permitido");
        }


        // Guardamos 
        transfer.setCreationDate(new Date(System.currentTimeMillis()));
        transferPort.save(transfer);

        // Si supera el límite y es corriente → pendiente
        if (transfer.getAmount().compareTo(getMaxAmount(origenAccountFull.getCurrencyType())) > 0
                && origenAccountFull.getAccountType() == AccountType.Current) {

            transfer.setTransferStatus(TransferStatus.Pending);
            transferPort.update(transfer);

        } else {

            // Cualquier otra transferencia válida se aprueba
            transfer.setTransferStatus(TransferStatus.Approved);
            transfer.setApprovalDate(new Date(System.currentTimeMillis()));
            transfer.setIdApprover(user.getDocument());

            transferPort.update(transfer);

            executeTransfer.executeTransfer(transfer);
        }


        // Recargar cuentas actualizadas
        BankAccount updatedOrigin =
            bankAccountPort.findByAccountNumber(origenAccountFull.getAccountNumber());

        BankAccount updatedDestination =
            bankAccountPort.findByAccountNumber(destinationAccountFull.getAccountNumber());

        // Bitácora
        Map<String, Object>  detailData = Map.of(
            "amountTransfer", transfer.getAmount(),

            "balanceBeforeOrigin", origenAccountFull.getCurrentBalance(),
            "balanceAfterOrigin", updatedOrigin.getCurrentBalance(),

            "balanceBeforeDestination", destinationAccountFull.getCurrentBalance(),
            "balanceAfterDestination", updatedDestination.getCurrentBalance()
        );
        
        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.CreationTransfer);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(transfer.getId());
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);

    }

    //Obtener el maximo segun la moneda:
    private BigDecimal getMaxAmount(CurrencyType currencyType){
        return switch (currencyType) {
            case COP -> BigDecimal.valueOf(10000000);
            case EUR -> BigDecimal.valueOf(2300);
            case USD -> BigDecimal.valueOf(2500);
            case GBP -> BigDecimal.valueOf(2000);
            default -> BigDecimal.ZERO;
        };
    }

    private BigDecimal getMinAmount(CurrencyType currencyType){
        return switch (currencyType) {
            case COP -> BigDecimal.valueOf(50);
            case EUR -> BigDecimal.valueOf(1);
            case USD -> BigDecimal.valueOf(1);
            case GBP -> BigDecimal.valueOf(1);
            default -> BigDecimal.ZERO;
        };
    }
}