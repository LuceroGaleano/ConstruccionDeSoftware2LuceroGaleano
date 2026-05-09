package app.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.Exception.NotFoundException;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.RolUser;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;

@Service
public class FindTransfer {
    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;

    @Autowired
    public FindTransfer(TransferPort transferPort, BankAccountPort bankAccountPort){
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
    }

    public Transfer findById(UUID id) throws NotFoundException{
        Transfer transfer = transferPort.findById(id);
        if(transfer == null){
            throw new NotFoundException("Transferencia no encontrada");
        }
        return transfer;
    }

    public List<Transfer> findByAccount(int accountNumber, User user) throws NotFoundException, BussinesException {
        BankAccount bankAccount = bankAccountPort.findByAccountNumber(accountNumber);
        if (bankAccount == null) {
            throw new NotFoundException("Cuenta no encontrada");
        }
        if(!bankAccount.getCustomerOwner().getDocument().equals(user.getDocument()) &&
        (user.getSystemRole().equals(RolUser.PersonCustomerUser) ||
        user.getSystemRole().equals(RolUser.CorporateCustomerUser))){
            throw new BussinesException("No puedes ver estas transferencias, no eres dueño del la cuenta bancaria");
        }
        return transferPort.findByOriginAccount(bankAccount);
    }
}
