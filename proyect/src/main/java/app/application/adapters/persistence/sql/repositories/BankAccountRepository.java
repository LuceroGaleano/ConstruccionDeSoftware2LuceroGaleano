package app.application.adapters.persistence.sql.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
    boolean existsByAccountNumber(int accountNumber);
    BankAccountEntity findByAccountNumber(int accountNumber);
    List<BankAccountEntity> findByCustomerOwner(CustomerEntity customer);
}
