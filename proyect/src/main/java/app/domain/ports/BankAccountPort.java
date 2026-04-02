package app.domain.ports;

import java.util.List;

import app.domain.models.BankAccount;
import app.domain.models.Customer;

public interface BankAccountPort {
    //find
    public BankAccount findById(String id);
    public List<BankAccount> findByCustomer(Customer customer);
    //exists
    public boolean existsByNumber(long accountNumber);
    //operation
    public void save(BankAccount bankAccount);
}
