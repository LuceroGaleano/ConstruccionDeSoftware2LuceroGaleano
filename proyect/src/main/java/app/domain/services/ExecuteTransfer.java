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
    
    public void executeTransfer(Transfer transfer) throws  BussinesException{
        if(transfer == null){
            throw new BussinesException("Transferencia no encontrada");
        }

        BankAccount origenAccount = transfer.getOriginAccount();
        BankAccount destinationAccount = transfer.getDestinationAccount();

        //Validamos que la cuenta de origen y cuenta destino no sea la misma
        if(origenAccount.getAccountNumber() == destinationAccount.getAccountNumber()){
            throw new BussinesException("No es posible realizar transferencia a la misma cuenta");
        }

        //Validamos que la cuenta de origen y destino este activa
        if(origenAccount.getAccountStatus() != AccountStatus.Active){
            throw new BussinesException("La cuenta de origen no puede enviar transferencias");
        }

        if(destinationAccount.getAccountStatus() != AccountStatus.Active){
            throw new BussinesException("La cuenta de destino no puede recibir transferencias");
        }

        //Validamos que la cuenta de origen tenga el saldo suficiente
        if(origenAccount.getCurrentBalance().compareTo(transfer.getAmount())<0){
            throw new BussinesException("Fondos insuficientes");
        }
        
        updateBalances(transfer);
        transfer.setTransferStatus(TransferStatus.Executed);
        transferPort.update(transfer);
    }

        //Si todo a salido bien en la transferencia, debemos modificar el monto de cuenta origen y desitno
    private void updateBalances(Transfer transfer){
        BankAccount origenAccount = transfer.getOriginAccount();
        BankAccount destinationAccount = transfer.getDestinationAccount();
        BigDecimal amount = transfer.getAmount();

        origenAccount.setCurrentBalance(origenAccount.getCurrentBalance().subtract(amount));
        destinationAccount.setCurrentBalance(destinationAccount.getCurrentBalance().add(amount));
        
        bankAccountPort.update(origenAccount);
        bankAccountPort.update(destinationAccount);
    }
}
