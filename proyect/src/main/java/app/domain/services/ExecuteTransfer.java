package app.domain.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;

@Service
public class ExecuteTransfer {
    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;

    @Autowired
    public ExecuteTransfer(TransferPort transferPort, BankAccountPort bankAccountPort){
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
    }
    
    public void executeTransfer(Transfer transfer) throws BussinesException {

        //Validamos qeu la transferencia exista
        if (transfer == null) {
            throw new BussinesException("Transferencia no encontrada");
        }

        BankAccount origenAccount = bankAccountPort.findByAccountNumber(
            transfer.getOriginAccount().getAccountNumber()
        );

        BankAccount destinationAccount = bankAccountPort.findByAccountNumber(
            transfer.getDestinationAccount().getAccountNumber()
        );

        //Validamos que las cuentas existan
        if (origenAccount == null || destinationAccount == null) {
            throw new BussinesException("Cuenta no encontrada");
        }

        //Validamos que ambas esten activas
        if (origenAccount.getAccountStatus() != AccountStatus.Active) {
            throw new BussinesException("La cuenta de origen no puede enviar transferencias");
        }

        if (destinationAccount.getAccountStatus() != AccountStatus.Active) {
            throw new BussinesException("La cuenta de destino no puede recibir transferencias");
        }

        //Validamos que tenga el suficiente sueldo
        if (origenAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new BussinesException("Fondos insuficientes");
        }

        updateBalances(origenAccount, destinationAccount, transfer);

        transfer.setTransferStatus(TransferStatus.Executed);
        transferPort.update(transfer);
    }

    //Si todo ha salido bien en la transferencia, debemos modificar el monto de cuenta origen y destino
    private void updateBalances(BankAccount origenAccount, BankAccount destinationAccount, Transfer transfer) {

        BigDecimal amount = transfer.getAmount();

        origenAccount.setCurrentBalance(origenAccount.getCurrentBalance().subtract(amount));
        destinationAccount.setCurrentBalance(destinationAccount.getCurrentBalance().add(amount));

        bankAccountPort.update(origenAccount);
        bankAccountPort.update(destinationAccount);
    }
}