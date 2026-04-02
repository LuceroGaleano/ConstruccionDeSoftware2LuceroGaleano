package app.domain.ports;

import java.util.List;

import app.domain.models.Customer;

public interface CustomerPort {
    //find
    public Customer findByDocument(String identification);
    public List<Customer> findAll();
    //exists
    public boolean existisByDocument(String identification);
    //operation
    public void save(Customer customer);
    public void update(Customer customer);
    public void deleteByDocument(String identification);
}
