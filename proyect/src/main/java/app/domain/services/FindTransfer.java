package app.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.NotFoundException;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
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

    public Transfer findById(String id) throws NotFoundException{
        Transfer transfer = transferPort.findById(id);
        if(transfer == null){
            throw new NotFoundException("Transferencia no encontrada");
        }
        return transfer;
    }

    public List<Transfer> findByAccount(UUID accountId) throws NotFoundException {
        BankAccount bankAccount = bankAccountPort.findById(accountId);
        if (bankAccount == null) {
            throw new NotFoundException("Cuenta no encontrada");
        }
        return transferPort.findByOriginAccount(bankAccount);
    }
}
