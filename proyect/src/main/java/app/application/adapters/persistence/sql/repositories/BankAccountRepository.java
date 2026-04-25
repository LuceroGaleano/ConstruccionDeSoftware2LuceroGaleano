package app.application.adapters.persistence.sql.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    boolean existsById(String id);
    boolean existsByAccountNumber(long accountNumber);
    BankAccountEntity findById(String id);  
    List<BankAccountEntity> findByCustomerOwner(CustomerEntity customer);
}
