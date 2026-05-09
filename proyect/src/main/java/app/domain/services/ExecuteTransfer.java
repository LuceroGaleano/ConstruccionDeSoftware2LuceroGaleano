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
    TransferPort transferPort;
    BankAccountPort bankAccountPort;

    @Autowired
    public ExecuteTransfer(TransferPort transferPort, BankAccountPort bankAccountPort){
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
    }
    
    public void executeTransfer(Transfer transfer) throws BussinesException {

        if (transfer == null) {
            throw new BussinesException("Transferencia no encontrada");
        }

        BankAccount origenAccount = bankAccountPort.findByAccountNumber(
            transfer.getOriginAccount().getAccountNumber()
        );

        BankAccount destinationAccount = bankAccountPort.findByAccountNumber(
            transfer.getDestinationAccount().getAccountNumber()
        );

        if (origenAccount == null || destinationAccount == null) {
            throw new BussinesException("Cuenta no encontrada");
        }


        if (origenAccount.getAccountStatus() != AccountStatus.Active) {
            throw new BussinesException("La cuenta de origen no puede enviar transferencias");
        }

        if (destinationAccount.getAccountStatus() != AccountStatus.Active) {
            throw new BussinesException("La cuenta de destino no puede recibir transferencias");
        }

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