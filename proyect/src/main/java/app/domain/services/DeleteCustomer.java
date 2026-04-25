package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.ports.CustomerPort;

@Service
public class DeleteCustomer {
    private final CustomerPort customerPort;

    @Autowired
    public DeleteCustomer(CustomerPort customerPort){
        this.customerPort = customerPort;
    }

    public void deleteCustomer(String document) throws BussinesException{
        if(!customerPort.existsByDocument(document)){
            throw new BussinesException("Cliente no encontrado");
        }
        customerPort.deleteByDocument(document);
    }
}
