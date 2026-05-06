package app.domain.ports;

import java.util.List;

import app.domain.models.Bitacora;

public interface BitacoraPort {
    public boolean existsById(String id);
    public Bitacora findById(String id);
    public void save(Bitacora bitacora);
    public List<Bitacora> findByUserDocument(String userDocument);
    public List<Bitacora> findByOperationType(String operationType);
}
