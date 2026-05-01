package app.application.usecases;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;
import app.domain.services.CreateCorporateCustomer;
import app.domain.services.CreatePersonCustomer;
import app.domain.services.FindCustomer;

@Service
public class WindowEmployeUseCase {
    private final CreatePersonCustomer createPersonCustomer;
    private final CreateCorporateCustomer createCorporateCustomer;
    private final FindCustomer findCustomer;

    public WindowEmployeUseCase(CreatePersonCustomer createPersonCustomer, CreateCorporateCustomer createCorporateCustomer, FindCustomer findCustomer) {
        this.createPersonCustomer = createPersonCustomer;
        this.createCorporateCustomer = createCorporateCustomer;
        this.findCustomer = findCustomer;
    }

    public void createPersonCustomer(PersonCustomer personCustomer) throws  BussinesException {
        createPersonCustomer.createPersonCustomer(personCustomer);
    }

    public void createCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException {
        createCorporateCustomer.createCorporateCustomer(corporateCustomer);
    }

    public Customer findCustomerByDocument(String document) throws BussinesException {
        return findCustomer.findCustomer(document);
    }
}
