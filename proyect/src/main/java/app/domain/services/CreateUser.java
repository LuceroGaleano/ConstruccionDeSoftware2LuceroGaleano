package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;
import app.domain.models.User;
import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;
import app.domain.ports.CustomerPort;
import app.domain.ports.UserPort;

@Service
public class CreateUser {
    private final UserPort userPort;
    private final CustomerPort customerPort;
    private final CreateCorporateCustomer createCorporateCustomer;
    private final CreatePersonCustomer createPersonCustomer;

    @Autowired
    public CreateUser(UserPort userPort, CustomerPort customerPort, CreateCorporateCustomer createCorporateCustomer, CreatePersonCustomer createPersonCustomer){
        this.userPort = userPort;
        this.customerPort = customerPort;
        this.createCorporateCustomer = createCorporateCustomer;
        this.createPersonCustomer = createPersonCustomer;
    }

    public void createUser(User user) throws BussinesException{
        // Validar credenciales únicas del Usuario
        if(userPort.existsByDocument(user.getDocument())){
            throw new BussinesException("Identificacion ya registrada");
        }

        if(userPort.existsByUserName(user.getUserName())){
            throw new BussinesException("Nombre de usuario ya existente");
        }

        if(userPort.existsByEmail(user.getEmail())){
            throw new BussinesException("Ya existe un usuario con ese email");
        }

        // Si el usuario es cliente, validar o crear el Customer
        if(user.getSystemRole() == RolUser.CorporateCustomerUser || user.getSystemRole() == RolUser.PersonCustomerUser){
            Customer customer = customerPort.findByDocument(user.getDocument());
            
            if(customer != null){
                // Si Customer existe validar que los datos coincidan
                if(!hasMatchingData(user, customer)){
                    throw new BussinesException("Ya existe un cliente con esta identificación pero los datos no coinciden");
                }
            } else{
                // Customer no existe hay crearlo
                if(user.getSystemRole() == RolUser.CorporateCustomerUser){
                    createCorporateCustomer.createCorporateCustomer((CorporateCustomer)user.getCustomer());
                } else{
                    createPersonCustomer.createPersonCustomer((PersonCustomer)user.getCustomer());
                }
            }
        }

        // Guardar el usuario con estado activo
        user.setUserStatus(UserStatus.Active);
        userPort.save(user);
    }

    private boolean hasMatchingData(User user, Customer customer){
        return customer.getFullName().equals(user.getFullName()) && 
               customer.getDocument().equals(user.getDocument()) &&
               customer.getEmail().equals(user.getEmail()) &&
               customer.getPhone().equals(user.getPhone()) &&
               customer.getAddress().equals(user.getAddress());
    }
}