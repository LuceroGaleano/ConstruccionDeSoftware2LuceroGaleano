package app.domain.ports;

import java.util.List;

import app.domain.models.BankAccount;
import app.domain.models.Customer;

public interface BankAccountPort {
    //find
    public BankAccount findById(String id);
    public List<BankAccount> findByCustomerOwner(Customer customer);
    //exists
    public boolean existsByAccountNumber(long accountNumber);
    public boolean existsById(String id);
    //operation
    public void save(BankAccount bankAccount);
    public void update(BankAccount bankAccount);
}
