package app.application.adapters.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.request.CorporateCustomerRequest;
import app.application.adapters.api.request.OnCreate;
import app.application.adapters.api.request.OnSearch;
import app.application.adapters.api.request.PersonCustomerRequest;
import app.application.adapters.api.response.CorporateCustomerResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.usecases.WindowEmployeUseCase;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;

@RestController
@RequestMapping("/window_employe")
public class WindowEmployeController {
    @Autowired
    private final WindowEmployeUseCase windowEmployeUseCase;

    public WindowEmployeController(WindowEmployeUseCase windowEmployeUseCase){
        this.windowEmployeUseCase = windowEmployeUseCase;
    }

    //Customer
    @PostMapping("/person_customer")
    public ResponseEntity<PersonCustomerResponse> createPersonCustomer(
        @Validated(OnCreate.class) @RequestBody PersonCustomerRequest request){
        PersonCustomer personCustomer = toPersonCustomer(request);
        windowEmployeUseCase.createPersonCustomer(personCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toPersonCustomerResponse(personCustomer));
    }

    @PostMapping("/coorporate_customer")
    public ResponseEntity<CorporateCustomerResponse> createCorporateCustomer(
        @Validated(OnSearch.class) @RequestBody CorporateCustomerRequest request){
        CorporateCustomer corporateCustomer = toCorporateCustomer(request);
        windowEmployeUseCase.createCorporateCustomer(corporateCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toCorporateCustomerResponse(corporateCustomer));
    }

    @GetMapping("/customer/{document}")
    public ResponseEntity<?> findCustomerByDocument(@PathVariable String document){
        Customer customer = windowEmployeUseCase.findCustomerByDocument(document);
        if (customer instanceof PersonCustomer person) {
            return ResponseEntity.ok(toPersonCustomerResponse(person));
        } else if (customer instanceof CorporateCustomer corporate) {
            return ResponseEntity.ok(toCorporateCustomerResponse(corporate));
        }
        return ResponseEntity.notFound().build();
    }

    //Mappers
    private static PersonCustomer toPersonCustomer(PersonCustomerRequest req){
        PersonCustomer personCustomer = new PersonCustomer();
        personCustomer.setFullName(req.getFullName());
        personCustomer.setDocument(req.getDocument());
        personCustomer.setEmail(req.getEmail());
        personCustomer.setPhone(req.getPhone());
        personCustomer.setAddress(req.getAddress());
        personCustomer.setRolCustomer(req.getRolCustomer());
        personCustomer.setCustomerStatus(req.getCustomerStatus());
        personCustomer.setBirthDate(req.getBirthDate());
        return personCustomer;
    }

    private static CorporateCustomer toCorporateCustomer(CorporateCustomerRequest req){
        CorporateCustomer corporateCustomer = new CorporateCustomer();
        corporateCustomer.setFullName(req.getFullName());
        corporateCustomer.setDocument(req.getDocument());
        corporateCustomer.setEmail(req.getEmail());
        corporateCustomer.setPhone(req.getPhone());
        corporateCustomer.setAddress(req.getAddress());
        corporateCustomer.setRolCustomer(req.getRolCustomer());
        corporateCustomer.setCustomerStatus(req.getCustomerStatus());
        if(req.getLegalRepresentative() != null){
            corporateCustomer.setLegalRepresentative(toPersonCustomer(req.getLegalRepresentative()));
        }
        return corporateCustomer;
    }

    private static PersonCustomerResponse toPersonCustomerResponse(PersonCustomer personCustomer){
        return new PersonCustomerResponse(
            personCustomer.getId(),
            personCustomer.getFullName(),
            personCustomer.getDocument(),
            personCustomer.getEmail(),
            personCustomer.getPhone(),
            personCustomer.getAddress(),
            personCustomer.getRolCustomer(),
            personCustomer.getCustomerStatus(),
            personCustomer.getBirthDate()
        );
    }

    private static CorporateCustomerResponse toCorporateCustomerResponse(CorporateCustomer corporateCustomer){
        return new CorporateCustomerResponse(
            corporateCustomer.getId(),
            corporateCustomer.getFullName(),
            corporateCustomer.getDocument(),
            corporateCustomer.getEmail(),
            corporateCustomer.getPhone(),
            corporateCustomer.getAddress(),
            corporateCustomer.getRolCustomer(),
            corporateCustomer.getCustomerStatus(),
            toPersonCustomerResponse(corporateCustomer.getLegalRepresentative())
        );
    }
}
