package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.models.enums.CustomerStatus;
import app.domain.ports.CustomerPort;

@Service
public class UpdateCorporateCustomer {
    private CustomerPort customerPort;
    private CreatePersonCustomer createPersonCustomer;

    @Autowired
    public UpdateCorporateCustomer(CustomerPort customerPort, CreatePersonCustomer createPersonCustomer){
        this.customerPort = customerPort;
        this.createPersonCustomer = createPersonCustomer;
    }

    public void updateCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException{
        if(!customerPort.existisByDocument(corporateCustomer.getIdentification())){
            throw new BussinesException("No existe una empresa con dicha NIT");
        }
        
        //Validar que la persona representante exista en la base de datos
        if(!customerPort.existisByDocument(corporateCustomer.getLegalRepresentative().getIdentification())){
            createPersonCustomer.createPersonCustomer(corporateCustomer.getLegalRepresentative());
        }

        //Validar que la persona representante este activo
        if(corporateCustomer.getLegalRepresentative().getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El representante legal no esta activo en el sistema");
        }

        customerPort.update(corporateCustomer);
    }
}
