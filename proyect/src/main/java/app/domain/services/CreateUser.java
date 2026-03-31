package app.domain.services;

import app.domain.Exception.BussinesException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.ports.CustomerPort;
import app.domain.ports.UserPort;

public class CreateUser {
    UserPort userPort;
    CustomerPort customerPort;

    public void createUser(User user) throws BussinesException{
        //Validar que la identificacion no exista en la base de datos
        //Si exista lanzamos la excepcion
        if(userPort.existisByDocument(user.getIdentification())){
            throw new BussinesException("Identificacion ya registrada");
        }


        //Validar que el userNmae no este repetido, si existe se lanza excepcion
        if(userPort.existisUserName(user.getUserName())){
            throw new BussinesException("Nombre de usuario ya existente");
        }

        //Si ya existe un cliente con la misma identificacion, debemos validar que los datos coincidan
        //Si no coinciden se lanza una excepcion
        if(customerPort.existisByDocument(user.getIdentification())){
            if(!hasMatchingData(user)){
                throw new BussinesException("Hemos encontrado un cliente con la misma identificacion, sin embargo, sus datos no coninciden");
            }
        }
        userPort.save(user);
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
