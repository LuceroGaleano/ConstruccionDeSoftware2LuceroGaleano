package app.domain.ports;

import java.util.List;
import java.util.UUID;

import app.domain.models.BankAccount;
import app.domain.models.Transfer;

public interface  TransferPort {
    public boolean existsById(UUID id);
    public Transfer findById(UUID id);
    public List<Transfer> findByOriginAccount(BankAccount bankAccount);
    public void save(Transfer transfere);
    public void update(Transfer transfer);
}
