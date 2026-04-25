package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.PersonCustomer;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import app.domain.ports.CustomerPort;

@Service
public class CreatePersonCustomer {
    private final CustomerPort customerPort;
    
    @Autowired
    public CreatePersonCustomer(CustomerPort customerPort){
        this.customerPort = customerPort;
    }

    public void createPersonCustomer(PersonCustomer personCustomer) throws BussinesException{
        //Validar que la identificación no exista en la base de datos
        if(customerPort.existsByDocument(personCustomer.getDocument())){
            throw new BussinesException("Identificación ya registrada");
        }

        personCustomer.setRolCustomer(RolCustomer.PersonCustomer);
        personCustomer.setCustomerStatus(CustomerStatus.Active);
        customerPort.save(personCustomer);
    }
}
 