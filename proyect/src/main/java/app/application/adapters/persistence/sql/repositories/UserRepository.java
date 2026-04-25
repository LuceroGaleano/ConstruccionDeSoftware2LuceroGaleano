package app.application.adapters.persistence.sql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.application.adapters.persistence.sql.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByDocument(String document);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    UserEntity findByDocument(String document);
    UserEntity findByUserName(String userName);
    UserEntity findByEmail(String email);
    void deleteByDocument(String document);
}
