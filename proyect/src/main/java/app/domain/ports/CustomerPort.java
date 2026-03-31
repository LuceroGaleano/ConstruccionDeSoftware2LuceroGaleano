package app.domain.ports;

import app.domain.models.Customer;

public interface CustomerPort {
    public boolean existisByDocument(String identification);
    public Customer findByDocument(String identification);
    public void save(Customer customer);
}
