package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.NotFoundException;
import app.domain.models.Bitacora;
import app.domain.ports.BitacoraPort;

@Service
public class FindBitacora {
    private BitacoraPort bitacoraPort;

    @Autowired
    public FindBitacora(BitacoraPort bitacoraPort){
        this.bitacoraPort = bitacoraPort;
    }

    public Bitacora findById(String idBitacora) throws NotFoundException{
        Bitacora bitacora = bitacoraPort.findById(idBitacora);
        if(bitacora == null){
            throw new NotFoundException("Bitacora no encontrada");
        }
        return bitacora;
    }
}
