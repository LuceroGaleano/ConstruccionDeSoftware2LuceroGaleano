package app.domain.services;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.TransferPort;

@Service
public class CreateTransfer{
    private TransferPort transferPort;

    @Autowired
    public CreateTransfer(TransferPort transferPort){
        this.transferPort = transferPort;
    }

    public  void createTransfer(Transfer transfer) throws  BussinesException{
        BankAccount origenAccount = transfer.getOriginAccount();
        BankAccount destinationAccount = transfer.getDestinationAccount();
        BigDecimal maxAmount = new BigDecimal(500000);

        //Validamos que se id unico
        if(transferPort.existisById(transfer.getIdTransfer())){
            throw new BussinesException("Ya existe una transferencia con el mismo");
        }

        //Validamos que cuenta origen y cuenta destino exista
        if(origenAccount == null){
            throw new BussinesException("No se ha encontrado la cuenta de origen");
        }

        if(destinationAccount == null){
            throw  new BussinesException("No se ha encontrado la cuenta de destino");
        }

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

        //Si el monto supera el limite y la cuenta es de tipo corriente, necesitara aprobacion
        if(transfer.getAmount().compareTo(maxAmount)>0 && origenAccount.getAccountType() == AccountType.Current){
            //!Servicio aprobar transferencia
            transfer.setTransferStatus(TransferStatus.Pending);
        } else{
            transfer.setTransferStatus(TransferStatus.Approved);
            updateBalances(transfer);
        }

        transfer.setOriginAccount(origenAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setCreationDate(new Date(System.currentTimeMillis()));
        transferPort.save(transfer);
    }


    //Si todo a salido bien en la transferencia, debemos modificar el monto de cuenta origen y desitno
    private void updateBalances(Transfer transfer){
        BankAccount origenAccount = transfer.getOriginAccount();
        BankAccount destinationAccount = transfer.getDestinationAccount();
        BigDecimal amount = transfer.getAmount();

        origenAccount.setCurrentBalance(origenAccount.getCurrentBalance().subtract(amount));
        destinationAccount.setCurrentBalance(destinationAccount.getCurrentBalance().add(amount));
        //!Falta actualizar ambas cuentas

    }
}
