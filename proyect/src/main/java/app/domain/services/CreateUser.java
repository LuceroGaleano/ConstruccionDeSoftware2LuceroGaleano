package app.domain.services;

import app.domain.Exception.BussinesException;
import app.domain.models.User;
import app.domain.ports.UserPort;

public class CreateUser {
    UserPort userPort;

    public void createUser(User user) throws BussinesException{
        if(userPort.existisByDocument(user.getIdNumber())){
            throw new BussinesException("Identificacion ya registrada");
        }

        if(userPort.existisUserName(user.getUserName())){
            throw new BussinesException("Nombre de usuario ya existente");
        }
    }
}
