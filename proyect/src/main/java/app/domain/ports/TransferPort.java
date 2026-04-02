package app.domain.ports;

import java.util.List;

import app.domain.models.Customer;
import app.domain.models.Transfer;

public interface  TransferPort {
    public Transfer findById(String Id);
    public List<Transfer> findByCustomer(Customer customer);
    public void save(Transfer transfere);
}
