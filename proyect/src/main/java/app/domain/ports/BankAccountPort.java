package app.domain.ports;

import java.util.List;
import java.util.UUID;

import app.domain.models.BankAccount;
import app.domain.models.Customer;

public interface BankAccountPort {
    //find
    public BankAccount findById(UUID id);
    public BankAccount findByAccountNumber(int accountNumber);
    public List<BankAccount> findByCustomerOwner(Customer customer);
    //exists
    public boolean existsByAccountNumber(int accountNumber);
    public boolean existsById(UUID id);
    //operation
    public void save(BankAccount bankAccount);
    public void update(BankAccount bankAccount);
}
