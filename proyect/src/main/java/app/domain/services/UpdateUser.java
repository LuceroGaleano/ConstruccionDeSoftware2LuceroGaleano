package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.ports.CustomerPort;
import app.domain.ports.UserPort;

@Service
public class UpdateUser {
    private UserPort userPort;
    private CustomerPort customerPort;
    
    @Autowired
    public UpdateUser(UserPort userPort, CustomerPort customerPort){
        this.userPort = userPort;
        this.customerPort = customerPort;
    }

    public void updateUser(User user) throws BussinesException{
        //Validar que el usuario exista
        if(!userPort.existisByDocument(user.getIdentification())){
            throw new BussinesException("No existe un usuario con ese documento");
        }

        //Validar que el userName no este repetido, si existe se lanza excepcion
        if(userPort.existisByUserName(user.getUserName())){
            throw new BussinesException("Nombre de usuario ya existente");
        }

        //Validar que el email no este repetido
        if(userPort.existsByEmail(user.getEmail())){
            throw new BussinesException("Ya existe un usuario con ese email");
        }

        //Si existe un cliente con la misma identificacion tambien debe de actualizar los datos del clienete
        //Si ya existe un cliente con la misma identificacion, debemos validar que los datos coincidan
        //Si no coinciden se lanza una excepcion
        if(customerPort.existisByDocument(user.getIdentification())){
            if(!hasMatchingData(user)){
                throw new BussinesException("Hemos encontrado un cliente con la misma identificacion, sin embargo, sus datos no coninciden");
            }
        }
        userPort.update(user);
    }

    //Metodo que compara la informacion del usuario la del cliente
    private boolean hasMatchingData(User user){
        Customer customer = customerPort.findByDocument(user.getIdentification());
        //Validar dato por dato que tienen en comun customer con userw
        boolean isConsistent = (customer.getFullName().equals(user.getFullName()) && 
        customer.getIdentification().equals(user.getIdentification()) &&
        customer.getEmail().equals(user.getEmail()) &&
        customer.getPhone().equals(user.getPhone()) &&
        customer.getAddress().equals(user.getAddress()));
        return isConsistent;
    }
}
