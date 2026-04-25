package app.domain.ports;

import java.util.List;

import app.domain.models.BankAccount;
import app.domain.models.Transfer;

public interface  TransferPort {
    public boolean existsById(String id);
    public Transfer findById(String Id);
    public List<Transfer> findByOriginAccount(BankAccount bankAccount);
    public void save(Transfer transfere);
    public void update(Transfer transfer);
}
