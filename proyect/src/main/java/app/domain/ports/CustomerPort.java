package app.domain.ports;

import app.domain.models.Customer;

public interface CustomerPort {
    public boolean existisByDocument(String idNumber);
    public void save(Customer customer);
}
