package app.domain.services;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;
import app.domain.ports.UserPort;

@Service
public class CreateTransfer{
    private TransferPort transferPort;
    private BankAccountPort bankAccountPort;
    private ExecuteTransfer executeTransfer;
    private UserPort userPort;

    @Autowired
    public CreateTransfer(TransferPort transferPort, BankAccountPort bankAccountPort, UserPort userPort, ExecuteTransfer executeTransfer){
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.userPort = userPort;
        this.executeTransfer = executeTransfer;
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

        //Validamos que el creador de la transferencia exista
        User createUser = userPort.findByDocument(transfer.getIdCreator());
        if(createUser == null){
            throw new BussinesException("No se ha encontrado el usuario creador de la transferencia");
        }

        //Si el monto supera el limite y la cuenta es de tipo corriente, necesitara aprobacion
        if(transfer.getAmount().compareTo(maxAmount)>0 && origenAccount.getAccountType() == AccountType.Current){
            //!Servicio aprobar transferencia
            transfer.setTransferStatus(TransferStatus.Pending);
        } else{
            transfer.setTransferStatus(TransferStatus.Approved);
            transfer.setApprovalDate(new Date(System.currentTimeMillis()));
            executeTransfer.executeTransfer(transfer);
        }



        transfer.setOriginAccount(origenAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setCreationDate(new Date(System.currentTimeMillis()));
        transferPort.save(transfer);
    }
}
