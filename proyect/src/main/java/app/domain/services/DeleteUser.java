package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.ports.UserPort;

@Service
public class DeleteUser {
    private UserPort userPort;

    @Autowired
    public DeleteUser(UserPort userPort){
        this.userPort = userPort;
    }

    public void deleteUser(String document)throws BussinesException{
        if(!userPort.existsByDocument(document)){
            throw new BussinesException("Usuario no encontrado");
        }
        userPort.deleteByDocument(document);
    }
}
