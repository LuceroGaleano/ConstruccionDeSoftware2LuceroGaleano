package app.application.adapters.persistence.sql.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.entities.TransferEntity;

public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {
    List<TransferEntity> findByOriginAccount(BankAccountEntity originAccount);
}
