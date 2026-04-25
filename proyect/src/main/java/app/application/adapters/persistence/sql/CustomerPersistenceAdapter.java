package app.application.adapters.persistence.sql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.CorporateCustomerEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;
import app.application.adapters.persistence.sql.entities.PersonCustomerEntity;
import app.application.adapters.persistence.sql.repositories.CustomerRepository;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;
import app.domain.ports.CustomerPort;

@Service
public class CustomerPersistenceAdapter implements CustomerPort {
    private final CustomerRepository customerRepository;

    public CustomerPersistenceAdapter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean existsByDocument(String document) {
        return customerRepository.existsByDocument(document);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(toEntity(customer));
    }

    @Override
    public void update(Customer customer) {
        CustomerEntity existingCustomer = customerRepository.findByDocument(customer.getDocument());
        if (existingCustomer != null) {
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setDocument(customer.getDocument());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setRolCustomer(customer.getRolCustomer() != null ? customer.getRolCustomer().toString() : null);
            existingCustomer.setCustomerStatus(customer.getCustomerStatus() != null ? customer.getCustomerStatus().toString() : null);
            customerRepository.save(existingCustomer);
        }
    }  // ← cierra update aquí

    @Override
    public Customer findByDocument(String document) {
        return toModel(customerRepository.findByDocument(document));
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByDocument(String document) {
        customerRepository.deleteByDocument(document);
    }

    private CustomerEntity toEntity(Customer customer) {
        if (customer == null) return null;
        CustomerEntity e;
        if (customer instanceof PersonCustomer pc) {
            PersonCustomerEntity pe = new PersonCustomerEntity();
            pe.setBirthDate(pc.getBirthDate());
            e = pe;
        } else {
            CorporateCustomerEntity ce = new CorporateCustomerEntity();
            e = ce;
        }
        e.setFullName(customer.getFullName());
        e.setDocument(customer.getDocument());
        e.setEmail(customer.getEmail());
        e.setPhone(customer.getPhone());
        e.setAddress(customer.getAddress());
        e.setRolCustomer(customer.getRolCustomer() != null ? customer.getRolCustomer().toString() : null);
        e.setCustomerStatus(customer.getCustomerStatus() != null ? customer.getCustomerStatus().toString() : null);
        return e;
    }

    private Customer toModel(CustomerEntity e) {
        if (e == null) return null;
        if (e instanceof PersonCustomerEntity) {
            PersonCustomer customer = new PersonCustomer();
            customer.setDocument(e.getDocument());
            customer.setFullName(e.getFullName());
            return customer;
        } else if (e instanceof CorporateCustomerEntity) {
            CorporateCustomer customer = new CorporateCustomer();
            customer.setDocument(e.getDocument());
            customer.setFullName(e.getFullName());
            return customer;
        }
        return null;
    }
}
