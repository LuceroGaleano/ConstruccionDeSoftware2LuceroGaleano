package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.PersonCustomer;
import app.domain.ports.CustomerPort;
import app.domain.ports.UserPort;

@Service
public class UpdatePersonCustomer {
    private final CustomerPort customerPort;
    private final UserPort userPort;

    @Autowired
    public UpdatePersonCustomer(CustomerPort customerPort, UserPort userPort){
        this.customerPort = customerPort;
        this.userPort = userPort;
    }

    public void updatePersonCustomer(PersonCustomer personCustomer) throws BussinesException{
        if(!customerPort.existsByDocument(personCustomer.getDocument())){
            throw  new BussinesException("No existe un cliente con esa identificación");
        }

        if(userPort.existsByDocument(personCustomer.getDocument())){
            userPort.update(userPort.findByDocument(personCustomer.getDocument()));
        }

        customerPort.update(personCustomer);
    }
}
