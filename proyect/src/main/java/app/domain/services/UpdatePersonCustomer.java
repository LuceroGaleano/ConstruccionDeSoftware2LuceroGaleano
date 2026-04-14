package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.PersonCustomer;
import app.domain.ports.CustomerPort;

@Service
public class UpdatePersonCustomer {
    private CustomerPort customerPort;

    @Autowired
    public UpdatePersonCustomer(CustomerPort customerPort){
        this.customerPort = customerPort;
    }

    public void updatePersonCustomer(PersonCustomer personCustomer) throws BussinesException{
        if(!customerPort.existisByDocument(personCustomer.getIdentification())){
            throw  new BussinesException("No existe un cliente con esa identificación");
        }

        customerPort.update(personCustomer);
    }
}
