package app.domain.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Bitacora;
import app.domain.models.User;
import app.domain.ports.BitacoraPort;
import app.domain.ports.UserPort;

@Service
public class CreateBitacora {
    private final BitacoraPort bitacoraPort;
    private final UserPort userPort;

    @Autowired
    public CreateBitacora(BitacoraPort bitacoraPort, UserPort userPort){
        this.bitacoraPort = bitacoraPort;
        this.userPort = userPort;
    }

    public void createBitacora(Bitacora bitacora) throws  BussinesException{
        // Validar que la bitácora no sea exista
        if (bitacora == null) {
            throw new BussinesException("Bitácora inválida");
        }

        //Validamos que el usuario registrado exista
        User user = userPort.findByDocument(bitacora.getUserDocument());
        if(user == null){
            throw new BussinesException("Usuario no encontrado");
        }

        //Validamos que el producto exista
        if (bitacora.getProductId() == null) {
            throw new BussinesException("Producto inválido");
        }

        bitacora.setOperationDate(new Date(System.currentTimeMillis()));
        bitacoraPort.save(bitacora);
    }
}
