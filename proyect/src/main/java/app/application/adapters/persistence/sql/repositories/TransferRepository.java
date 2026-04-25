package app.application.adapters.persistence.sql.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.entities.TransferEntity;

public interface TransferRepository extends JpaRepository<TransferEntity, String> {
    List<TransferEntity> findByOriginAccount(BankAccountEntity originAccount);
}
