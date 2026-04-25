package app.application.adapters.persistence.sql;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.BitacoraEntity;
import app.application.adapters.persistence.sql.repositories.BitacoraRepository;
import app.domain.models.Bitacora;
import app.domain.ports.BitacoraPort;

@Service
public class BitacoraPersistenceAdapter implements BitacoraPort {
    private final BitacoraRepository bitacoraRepository;

    public BitacoraPersistenceAdapter(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @Override
    public void save(Bitacora bitacora) {
        bitacoraRepository.save(toEntity(bitacora));
    }

    @Override
    public boolean existsById(String id) {
        return bitacoraRepository.existsById(id);
    }

    @Override
    public Bitacora findById(String id) {
        return bitacoraRepository.findById(id)
        .map(this::toModel)
        .orElse(null);
    }

    private BitacoraEntity toEntity(Bitacora bitacora) {
        BitacoraEntity e = new BitacoraEntity();
        e.setId(bitacora.getId());
        e.setOperationType(bitacora.getOperationType() != null ? bitacora.getOperationType() : null);
        e.setOperationDate(bitacora.getOperationDate());
        return e;
    }

    private Bitacora toModel(BitacoraEntity e){
        if(e == null) return null;
        Bitacora bitacora = new Bitacora();
        bitacora.setId(e.getId());
        bitacora.setOperationDate(e.getOperationDate());
        bitacora.setOperationType(e.getOperationType() != null ? e.getOperationType() : null);
        return bitacora;
    }
}
