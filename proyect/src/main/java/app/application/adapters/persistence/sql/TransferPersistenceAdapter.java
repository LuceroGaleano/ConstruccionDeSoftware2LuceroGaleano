package app.application.adapters.persistence.sql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.entities.TransferEntity;
import app.application.adapters.persistence.sql.repositories.BankAccountRepository;
import app.application.adapters.persistence.sql.repositories.TransferRepository;
import app.domain.models.BankAccount;
import app.domain.models.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.TransferPort;

@Service
public class TransferPersistenceAdapter implements TransferPort {
    private final TransferRepository transferRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransferPersistenceAdapter(TransferRepository transferRepository, BankAccountRepository bankAccountRepository) {
        this.transferRepository = transferRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void save(Transfer transfer){
        transferRepository.save(toEntity(transfer));
    }

    @Override
    public void update(Transfer transfer){
        TransferEntity existingTransfer = transferRepository.findById(transfer.getId()).orElse(null);
        if(existingTransfer != null){
            existingTransfer.setAmount(transfer.getAmount());
            existingTransfer.setCreationDate(transfer.getCreationDate());
            existingTransfer.setApprovalDate(transfer.getApprovalDate());
            existingTransfer.setTransferStatus(transfer.getTransferStatus() != null ? transfer.getTransferStatus().toString() : null);
            existingTransfer.setIdCreator(transfer.getIdCreator());
            existingTransfer.setIdApprover(transfer.getIdApprover());
            transferRepository.save(existingTransfer);
        }
    }

    @Override
    public boolean existsById(String id){
        return transferRepository.existsById(id);
    }

    @Override 
    public Transfer findById(String id){
        return transferRepository.findById(id).map(this::toModel).orElse(null);
    }

    @Override
    public List<Transfer> findByOriginAccount(BankAccount bankAccount) {
        BankAccountEntity accountEntity = bankAccountRepository.findById(bankAccount.getId());
        return transferRepository.findByOriginAccount(accountEntity).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private Transfer toModel(TransferEntity e){
        Transfer t = new Transfer();
        t.setId(e.getId());
        t.setAmount(e.getAmount());
        t.setCreationDate(e.getCreationDate());
        t.setApprovalDate(e.getApprovalDate());
        t.setTransferStatus(e.getTransferStatus() != null ? TransferStatus.valueOf(e.getTransferStatus()) : null);
        t.setIdCreator(e.getIdCreator());
        t.setIdApprover(e.getIdApprover());
        return t;
    }

    private TransferEntity toEntity(Transfer transfer){
        TransferEntity e = new TransferEntity();
        e.setAmount(transfer.getAmount());
        e.setCreationDate(transfer.getCreationDate());
        e.setApprovalDate(transfer.getApprovalDate());
        e.setTransferStatus(transfer.getTransferStatus() != null ? transfer.getTransferStatus().toString() : null);
        e.setIdCreator(transfer.getIdCreator());
        e.setIdApprover(transfer.getIdApprover());
        if(transfer.getOriginAccount() != null){
            e.setOriginAccount(bankAccountRepository.findById(transfer.getOriginAccount().getId()));
        }
        if(transfer.getDestinationAccount() != null){
            e.setDestinationAccount(bankAccountRepository.findById(transfer.getDestinationAccount().getId()));
        }
        return e;
    }
}