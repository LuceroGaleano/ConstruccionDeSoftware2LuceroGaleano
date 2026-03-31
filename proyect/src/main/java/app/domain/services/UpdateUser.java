package app.domain.services;

import app.domain.Exception.BussinesException;
import app.domain.models.User;
import app.domain.ports.UserPort;

public class UpdateUser {
    UserPort userPort;
    public void updateUser(User user) throws BussinesException{
        //Buscar el usuario en la base de datos por identificacion
        User userModific = userPort.findByDocument(user.getIdentification());
        
        //Si no existe se lanza una excepcion
        if(userModific == null){
            throw new BussinesException("El usuario no existe");
        }

        //Validar que el userName nuevo no este repetido entre los usuario
        //Si esta repetido lanza una excepcion
        //Ademas debemos validar que si haya cambiado el nombre de usuario
        if(userPort.existisUserName(user.getUserName()) && !userModific.getUserName().equals(user.getUserName())){
            throw new BussinesException("Nombre de usuario ya existente");
        }

        userPort.save(user);
    }
}
