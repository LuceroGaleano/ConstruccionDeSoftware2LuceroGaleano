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
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
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
            if (customer.getFullName() != null) existingCustomer.setFullName(customer.getFullName());
            if (customer.getEmail() != null) existingCustomer.setEmail(customer.getEmail());
            if (customer.getPhone() != null) existingCustomer.setPhone(customer.getPhone());
            if (customer.getAddress() != null) existingCustomer.setAddress(customer.getAddress());
            if (customer.getRolCustomer() != null) existingCustomer.setRolCustomer(customer.getRolCustomer().toString());
            if (customer.getCustomerStatus() != null) existingCustomer.setCustomerStatus(customer.getCustomerStatus().toString());

            // Si es corporateCustomer actualizar el representante legal
            if (customer instanceof CorporateCustomer cc && existingCustomer instanceof CorporateCustomerEntity ce) {
                if (cc.getLegalRepresentative() != null && cc.getLegalRepresentative().getDocument() != null) {
                    PersonCustomerEntity legalEntity = (PersonCustomerEntity) customerRepository
                            .findByDocument(cc.getLegalRepresentative().getDocument());
                    if (legalEntity != null) ce.setLegalRepresentative(legalEntity);
                }
            }

            customerRepository.save(existingCustomer);
        }
    }

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
        } else if (customer instanceof CorporateCustomer cc) {
            CorporateCustomerEntity ce = new CorporateCustomerEntity();
            if (cc.getLegalRepresentative() != null) {
                // ✅ Busca la entidad existente en la BD
                PersonCustomerEntity legalEntity = (PersonCustomerEntity) customerRepository
                    .findByDocument(cc.getLegalRepresentative().getDocument());
                ce.setLegalRepresentative(legalEntity);
            }
            e = ce;
        } else {
            e = new CustomerEntity();
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
        if (e instanceof PersonCustomerEntity pe) {
            PersonCustomer customer = new PersonCustomer();
            customer.setId(pe.getId());
            customer.setFullName(pe.getFullName());
            customer.setDocument(pe.getDocument());
            customer.setEmail(pe.getEmail());
            customer.setPhone(pe.getPhone());
            customer.setAddress(pe.getAddress());
            customer.setBirthDate(pe.getBirthDate());
            customer.setRolCustomer(pe.getRolCustomer() != null ? RolCustomer.valueOf(pe.getRolCustomer()) : null);
            customer.setCustomerStatus(pe.getCustomerStatus() != null ? CustomerStatus.valueOf(pe.getCustomerStatus()) : null);
            return customer;
        } else if (e instanceof CorporateCustomerEntity ce) {
            CorporateCustomer customer = new CorporateCustomer();
            customer.setId(ce.getId());
            customer.setFullName(ce.getFullName());
            customer.setDocument(ce.getDocument());
            customer.setEmail(ce.getEmail());
            customer.setPhone(ce.getPhone());
            customer.setAddress(ce.getAddress());
            customer.setRolCustomer(ce.getRolCustomer() != null ? RolCustomer.valueOf(ce.getRolCustomer()) : null);
            customer.setCustomerStatus(ce.getCustomerStatus() != null ? CustomerStatus.valueOf(ce.getCustomerStatus()) : null);
            if (ce.getLegalRepresentative() != null) {
                customer.setLegalRepresentative((PersonCustomer) toModel(ce.getLegalRepresentative()));
            }
            return customer;
        }
        return null;
    }
}