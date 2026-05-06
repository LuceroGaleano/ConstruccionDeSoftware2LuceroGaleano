package app.application.adapters.persistence.mongodb.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.application.adapters.persistence.mongodb.documents.BitacoraDocument;

public interface BitacoraMongoRepository extends MongoRepository<BitacoraDocument, String> {
    List<BitacoraDocument> findByUserDocument(String userDocument);
    List<BitacoraDocument> findByOperationType(String operationType);
}