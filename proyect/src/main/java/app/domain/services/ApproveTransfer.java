package app.domain.services;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Bitacora;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.OperationBitacora;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.BitacoraPort;
import app.domain.ports.TransferPort;
import app.domain.ports.UserPort;

@Service
public class ApproveTransfer {

    private final TransferPort transferPort;
    private final ExecuteTransfer executeTransfer;
    private final UserPort userPort;
    private final BitacoraPort bitacoraPort;
    private final BankAccountPort bankAccountPort;

    @Autowired
    public ApproveTransfer(
            TransferPort transferPort,
            ExecuteTransfer executeTransfer,
            UserPort userPort,
            BitacoraPort bitacoraPort,
            BankAccountPort bankAccountPort) {

        this.transferPort = transferPort;
        this.executeTransfer = executeTransfer;
        this.userPort = userPort;
        this.bitacoraPort = bitacoraPort;
        this.bankAccountPort = bankAccountPort;
    }

    public void approveTransfer(UUID id, User user) throws BussinesException {

        Transfer transfer = transferPort.findById(id);

        // Validamos que la transferencia exista
        if (transfer == null) {
            throw new BussinesException("Transferencia no encontrada");
        }

        // Validamos que esté pendiente
        if (!transfer.getTransferStatus().equals(TransferStatus.Pending)) {
            throw new BussinesException("Estado no válido");
        }

        // Validamos usuario autenticado
        if (user == null) {
            throw new BussinesException("Usuario inválido");
        }

        User approver = user;

        // Validamos creador
        User creator = userPort.findByDocument(transfer.getIdCreator());

        if (creator == null) {
            throw new BussinesException("Usuario creador no encontrado");
        }

        // Validamos misma empresa
        if (!approver.getCustomer().getDocument()
                .equals(creator.getCustomer().getDocument())) {

            throw new BussinesException(
                    "El usuario aprobador y el creador no pertenecen a la misma empresa");
        }

        // Obtener cuentas
        BankAccount originAccount = transfer.getOriginAccount();
        BankAccount destinationAccount = transfer.getDestinationAccount();

        // Cargar saldos antes
        BankAccount originBefore =
                bankAccountPort.findByAccountNumber(originAccount.getAccountNumber());

        BankAccount destinationBefore =
                bankAccountPort.findByAccountNumber(destinationAccount.getAccountNumber());

        // Aprobar transferencia
        transfer.setTransferStatus(TransferStatus.Approved);
        transfer.setApprovalDate(new Date(System.currentTimeMillis()));
        transfer.setIdApprover(approver.getDocument());

        executeTransfer.executeTransfer(transfer);
        transferPort.update(transfer);

        // Cargar saldos después
        BankAccount updatedOrigin =
                bankAccountPort.findByAccountNumber(originAccount.getAccountNumber());

        BankAccount updatedDestination =
                bankAccountPort.findByAccountNumber(destinationAccount.getAccountNumber());

        // Bitacora
        Map<String, Object> detailData = Map.of(

                "amountTransfer", transfer.getAmount(),

                "balanceBeforeOrigin", originBefore.getCurrentBalance(),
                "balanceAfterOrigin", updatedOrigin.getCurrentBalance(),

                "balanceBeforeDestination", destinationBefore.getCurrentBalance(),
                "balanceAfterDestination", updatedDestination.getCurrentBalance()
        );

        // Crear bitácora
        Bitacora bitacora = new Bitacora();

        bitacora.setOperationType(OperationBitacora.ApprovingTransfer);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(transfer.getId());
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);
    }
}