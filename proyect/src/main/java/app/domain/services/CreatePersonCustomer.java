package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.PersonCustomer;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import app.domain.ports.CustomerPort;

@Service
public class CreatePersonCustomer {
    private CustomerPort customerPort;

    public void createPersonCustomer(PersonCustomer personCustomer) throws BussinesException{
        //Validar que la identificacion no exista en la base de datos
        if(customerPort.existisByDocument(personCustomer.getIdentification())){
            throw new BussinesException("Identificacion ya registrada");
        }

        personCustomer.setRolCustomer(RolCustomer.PersonCustomer);
        personCustomer.setCustomerStatus(CustomerStatus.Active);
        customerPort.save(personCustomer);
    }
}
 