package app.application.adapters.api.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.request.CustomerRequest;
import app.application.adapters.api.request.LoanRequest;
import app.application.adapters.api.response.LoanResponse;
import app.application.usecases.PersonCustomerUserUseCase;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/person_customer_user")
public class PersonCustomerController {

    @Autowired
    private final PersonCustomerUserUseCase personCustomerUserUseCase;

    public PersonCustomerController(PersonCustomerUserUseCase personCustomerUserUseCase){
        this.personCustomerUserUseCase = personCustomerUserUseCase;
    }

    // Loan
    @PostMapping("/loans")
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request){
        Loan loan = toLoan(request);
        User user = new User();
        user.setCustomer(loan.getCustomerOwner());
        personCustomerUserUseCase.createLoan(user, loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(toLoanResponse(loan));
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> findLoanById(@PathVariable String id){
        Loan loan = personCustomerUserUseCase.findByIdLoan(id);
        return ResponseEntity.ok(toLoanResponse(loan));

    }

    // Mappers
    private static Loan toLoan(LoanRequest req){
        Loan loan = new Loan();
        loan.setId(req.getId());
        loan.setProductName(req.getProductName());
        loan.setProductCategory(req.getProductCategory());
        if(req.getCustomerOwner() != null){
            loan.setCustomerOwner(toCustomer(req.getCustomerOwner()));
        }
        loan.setLoanType(req.getLoanType());
        loan.setRequestedAmount(req.getRequestedAmount());
        loan.setApprovalDate(req.getApprovalDate());
        loan.setInterestRate(req.getInterestRate());
        loan.setApproved(req.isApproved());
        loan.setTermInMonths(req.getTermInMonths());
        loan.setLoanStatus(req.getLoanStatus());
        loan.setCreateDate(req.getCreateDate());
        loan.setApprovalDate(req.getApprovalDate());
        loan.setDisburseDate(req.getDisburseDate());
        loan.setDisburseAccount(req.getDisburseAccount());
        return loan;
    }

    private static LoanResponse toLoanResponse(Loan loan){
        return new LoanResponse(
            loan.getId(),
            loan.getProductName(),
            loan.getProductCategory(),
            loan.isApproved(),
            loan.getCustomerOwner(),
            loan.getLoanType(),
            loan.getCustomerOwner(),
            loan.getRequestedAmount(),
            loan.getApprovedAmount(),
            loan.getInterestRate(),
            loan.getTermInMonths(),
            loan.getLoanStatus(),
            loan.getCreateDate(),
            loan.getApprovalDate(),
            loan.getDisburseDate(),
            loan.getDisburseAccount()
        );
    }



    private static Customer toCustomer(CustomerRequest req){
        PersonCustomer personCustomer = new PersonCustomer();
        personCustomer.setFullName(req.getFullName());
        personCustomer.setDocument(req.getDocument());
        personCustomer.setEmail(req.getEmail());
        personCustomer.setPhone(req.getPhone());
        personCustomer.setAddress(req.getAddress());
        personCustomer.setRolCustomer(req.getRolCustomer());
        personCustomer.setCustomerStatus(req.getCustomerStatus());
        personCustomer.setListProducts(new ArrayList<>(req.getListProducts()));
        return personCustomer;
    }
}
