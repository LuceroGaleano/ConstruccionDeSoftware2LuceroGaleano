package app.domain.services;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.ports.CustomerPort;

public class CreateCorporateCustomer {
    CustomerPort customerPort;
    CreatePersonCustomer createPersonCustomer;

    public void createCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException{
        if(customerPort.existisByDocument(corporateCustomer.getIdNumber())){
            throw new  BussinesException("NIT ya registrado en el sistema");
        }

        if(!customerPort.existisByDocument(corporateCustomer.getLegalRepresentative().getIdNumber())){
            createPersonCustomer.createPersonCustomer(corporateCustomer.getLegalRepresentative());
        }

        customerPort.save(corporateCustomer);
    }
}
