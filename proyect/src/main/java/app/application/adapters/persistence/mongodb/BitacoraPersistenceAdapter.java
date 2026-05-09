package app.application.adapters.persistence.mongodb;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.mongodb.documents.BitacoraDocument;
import app.application.adapters.persistence.mongodb.repositories.BitacoraMongoRepository;
import app.domain.models.Bitacora;
import app.domain.ports.BitacoraPort;

@Service
public class BitacoraPersistenceAdapter implements BitacoraPort {
    private final BitacoraMongoRepository repository;

    public BitacoraPersistenceAdapter(BitacoraMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Bitacora bitacora) {
        repository.save(toDocument(bitacora));
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public Bitacora findById(String id) {
        return repository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    @Override
    public List<Bitacora> findByUserDocument(String userDocument) {
        return repository.findByUserDocument(userDocument)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Bitacora> findByOperationType(String operationType) {
        return repository.findByOperationType(operationType)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private BitacoraDocument toDocument(Bitacora bitacora) {
        BitacoraDocument doc = new BitacoraDocument();
        doc.setOperationType(bitacora.getOperationType() != null ? bitacora.getOperationType().toString() : null);
        doc.setOperationDate(bitacora.getOperationDate());
        doc.setUserDocument(bitacora.getUserDocument());
        doc.setRolUser(bitacora.getRolUser() != null ? bitacora.getRolUser().toString() : null);
        doc.setProductId(bitacora.getProductId().toString());
        doc.setDetailData(bitacora.getDetailData());
        return doc;
    }

    private Bitacora toModel(BitacoraDocument doc) {
        Bitacora bitacora = new Bitacora();
        bitacora.setId(doc.getId());
        bitacora.setOperationDate(doc.getOperationDate());
        bitacora.setUserDocument(doc.getUserDocument());
        bitacora.setProductId(UUID.fromString(doc.getProductId()));
        bitacora.setDetailData(doc.getDetailData());
        return bitacora;
    }
}

