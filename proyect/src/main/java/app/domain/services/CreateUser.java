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
    public CreateUser(UserPort userPort, CustomerPort customerPort,
            CreateCorporateCustomer createCorporateCustomer,
            CreatePersonCustomer createPersonCustomer) {
        this.userPort = userPort;
        this.customerPort = customerPort;
        this.createCorporateCustomer = createCorporateCustomer;
        this.createPersonCustomer = createPersonCustomer;
    }

    public void createUser(User user) throws BussinesException {
        // Validar que el usuario exista
        if (user == null) {
            throw new BussinesException("Usuario no encontrado");
        }

        // Validar credenciales únicas del Usuario
        if (userPort.existsByDocument(user.getDocument())) {
            throw new BussinesException("Identificacion ya registrada");
        }

        if (userPort.existsByUserName(user.getUserName())) {
            throw new BussinesException("Nombre de usuario ya existente");
        }

        if (userPort.existsByEmail(user.getEmail())) {
            throw new BussinesException("Ya existe un usuario con ese email");
        }

        // Si el usuario es cliente, validar o crear el Customer
        if (user.getSystemRole() == RolUser.CorporateCustomerUser
                || user.getSystemRole() == RolUser.PersonCustomerUser) {

            if (user.getCustomer() == null || user.getCustomer().getDocument() == null) {
                throw new BussinesException("Debe proporcionar el documento del cliente");
            }

            // Buscar por documento del customer
            Customer customer = customerPort.findByDocument(user.getCustomer().getDocument());

            if (customer != null) {
                // Cliente ya existe, vincularlo
                user.setCustomer(customer);
            } else {
                // Cliente no existe, crearlo con los datos enviados
                if (user.getSystemRole() == RolUser.CorporateCustomerUser) {
                    if (!(user.getCustomer() instanceof CorporateCustomer cc)) {
                        throw new BussinesException("El cliente no existe, envíe todos sus datos para crearlo");
                    }
                    if (cc.getFullName() == null || cc.getEmail() == null) {
                        throw new BussinesException("El cliente no existe, envíe fullName y email para crearlo");
                    }
                    if (cc.getLegalRepresentative() == null || cc.getLegalRepresentative().getDocument() == null) {
                        throw new BussinesException("Debe proporcionar el representante legal del cliente corporativo");
                    }
                    createCorporateCustomer.createCorporateCustomer(cc);
                    user.setCustomer(cc);
                } else if(user.getSystemRole() == RolUser.PersonCustomerUser) {
                    if (!(user.getCustomer() instanceof PersonCustomer pc)) {
                        throw new BussinesException("El cliente no existe, envíe todos sus datos para crearlo");
                    }
                    if (pc.getFullName() == null || pc.getEmail() == null || pc.getBirthDate() == null) {
                        throw new BussinesException("El cliente no existe, envíe fullName, email y birthDate para crearlo \n customer:NIT");
                    }
                    createPersonCustomer.createPersonCustomer(pc);
                    user.setCustomer(pc);
                } 
            }
        }

        //Si el usuairo es de tipo CorporateEmploye o CorporateSupervisro
        //Se usara el customer para enlazar el id de la empresa para la que trabaja
         //Para poder validar que solo pueda trabajar con los productos de empresa
        if (user.getSystemRole() == RolUser.CorporateEmployee || user.getSystemRole() == RolUser.CorporateSupervisor) {
            if (user.getCustomer() == null || user.getCustomer().getDocument() == null) {
                throw new BussinesException("Para este tipo de rol, debe proporcionar el NIT de la empresa para la que trabaja");
            }
            Customer customer = customerPort.findByDocument(user.getCustomer().getDocument());
            if (customer == null) {
                throw new BussinesException("Empresa no encontrada");
            }
            if (!(customer instanceof CorporateCustomer)) {
                throw new BussinesException("El documento proporcionado no corresponde a una empresa");
            }
            user.setCustomer(customer);
        }

        // Guardar el usuario con estado activo
        user.setUserStatus(UserStatus.Active);
        userPort.save(user);
    }
}