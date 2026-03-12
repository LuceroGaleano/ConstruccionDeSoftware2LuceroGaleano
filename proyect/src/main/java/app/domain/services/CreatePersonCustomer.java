package app.domain.services;

import app.domain.Exception.BussinesException;
import app.domain.models.PersonCustomer;
import app.domain.ports.CustomerPort;

public class CreatePersonCustomer {
    private CustomerPort customerPort;

    public void createPersonCustomer(PersonCustomer personCustomer) throws BussinesException{
        if(customerPort.existisByDocument(personCustomer.getIdNumber())){
            throw new BussinesException("Identificacion ya registrada");
        }

        customerPort.save(personCustomer);
    }
}
 