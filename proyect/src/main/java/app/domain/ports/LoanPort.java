package app.domain.ports;

import java.util.List;
import java.util.UUID;

import app.domain.models.Customer;
import app.domain.models.Loan;

public interface LoanPort {
    //find
    public Loan findById(UUID id);
    public List<Loan> findByCustomer(Customer customer);
    //exists
    public boolean existsById(UUID id);
    //operation
    public void save(Loan loan);
    public void update(Loan loan);
}