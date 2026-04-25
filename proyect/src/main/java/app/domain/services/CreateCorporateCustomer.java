package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import app.domain.ports.CustomerPort;

@Service
public class CreateCorporateCustomer {
    private final CustomerPort customerPort;
    private final CreatePersonCustomer createPersonCustomer;

    @Autowired
    public CreateCorporateCustomer(CustomerPort customerPort, 
        CreatePersonCustomer createPersonCustomer){
        this.customerPort = customerPort;
        this.createPersonCustomer = createPersonCustomer;
    }

    public void createCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException{
        //Validar que el NIT no exista en la base de datos
        if(customerPort.existsByDocument(corporateCustomer.getDocument())){
            throw new  BussinesException("NIT ya registrado en el sistema");
        }

        //Validar que la persona representante exista en la base de datos
        if(!customerPort.existsByDocument(corporateCustomer.getLegalRepresentative().getDocument())){
            createPersonCustomer.createPersonCustomer(corporateCustomer.getLegalRepresentative());
        }

        //Validar que la persona representante este activo
        if(corporateCustomer.getLegalRepresentative().getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El representante legal no esta activo en el sistema");
        }

        corporateCustomer.setRolCustomer(RolCustomer.CorporateCustomer);
        corporateCustomer.setCustomerStatus(CustomerStatus.Active);
        customerPort.save(corporateCustomer);
    }
}
