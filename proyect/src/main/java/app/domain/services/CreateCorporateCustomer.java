package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.models.PersonCustomer;
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
            PersonCustomer legal = (PersonCustomer) corporateCustomer.getLegalRepresentative();
            if(legal.getFullName() == null || legal.getEmail() == null || legal.getBirthDate() == null){
                throw new BussinesException("El representante legal no existe, envíe todos sus datos para crearlo");
            }
            createPersonCustomer.createPersonCustomer(legal);
        }

        PersonCustomer legal = (PersonCustomer) customerPort.findByDocument(corporateCustomer.getLegalRepresentative().getDocument());
        //Validar que la persona representante este activo
        if(legal.getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El representante legal no esta activo en el sistema");
        }
        
        corporateCustomer.setLegalRepresentative((PersonCustomer) customerPort.findByDocument(corporateCustomer.getLegalRepresentative().getDocument()));
        corporateCustomer.setRolCustomer(RolCustomer.CorporateCustomer);
        corporateCustomer.setCustomerStatus(CustomerStatus.Active);
        customerPort.save(corporateCustomer);
    }
}
