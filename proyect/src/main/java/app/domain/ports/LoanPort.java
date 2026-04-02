package app.domain.ports;

import java.util.List;

import app.domain.models.Customer;
import app.domain.models.Loan;

public interface LoanPort {
    public Loan findById(String id);
    public List<Loan> findByCustomer(Customer customer);
    //operation
    public void save(Loan loan);
}
