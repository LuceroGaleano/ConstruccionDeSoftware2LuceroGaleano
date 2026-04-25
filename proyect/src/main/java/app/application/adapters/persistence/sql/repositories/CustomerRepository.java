package app.application.adapters.persistence.sql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.application.adapters.persistence.sql.entities.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    CustomerEntity findByDocument(String document);
    boolean existsByDocument(String document);
    void deleteByDocument(String document);
}
