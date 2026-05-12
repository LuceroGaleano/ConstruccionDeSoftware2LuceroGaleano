package app.application.adapters.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.request.BankAccountRequest;
import app.application.adapters.api.request.CorporateCustomerRequest;
import app.application.adapters.api.request.LoanRequest;
import app.application.adapters.api.request.OnCreate;
import app.application.adapters.api.request.OnSearch;
import app.application.adapters.api.request.PersonCustomerRequest;
import app.application.adapters.api.response.BankAccountResponse;
import app.application.adapters.api.response.CorporateCustomerResponse;
import app.application.adapters.api.response.CustomerResponse;
import app.application.adapters.api.response.LoanResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.usecases.SalesEmployeUseCase;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.User;
import app.domain.ports.UserPort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales_employe")
public class SalesEmployeController {

    @Autowired
    private final SalesEmployeUseCase salesEmployeUseCase;
    private final UserPort userPort;

    public SalesEmployeController(SalesEmployeUseCase salesEmployeUseCase, UserPort userPort) {
        this.salesEmployeUseCase = salesEmployeUseCase;
        this.userPort = userPort;
    }

    private User getAuthenticatedUser() {
        String document = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userPort.findByDocument(document);
    }

    // ── Customers ─────────────────────────────────────────────────────────────

    @PostMapping("/person_customer")
    public ResponseEntity<PersonCustomerResponse> createPersonCustomer(
            @Validated(OnCreate.class) @RequestBody PersonCustomerRequest request) {
        PersonCustomer personCustomer = toPersonCustomer(request);
        salesEmployeUseCase.createPersonCustomer(personCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toPersonCustomerResponse(personCustomer));
    }

    @PostMapping("/corporate_customer")
    public ResponseEntity<CorporateCustomerResponse> createCorporateCustomer(
            @Validated(OnSearch.class) @RequestBody CorporateCustomerRequest request) {
        CorporateCustomer corporateCustomer = toCorporateCustomer(request);
        salesEmployeUseCase.createCorporateCustomer(corporateCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toCorporateCustomerResponse(corporateCustomer));
    }

    @GetMapping("/customer/{document}")
    public ResponseEntity<?> findCustomerByDocument(@PathVariable String document) {
        Customer customer = salesEmployeUseCase.findCustomerByDocument(document);
        if (customer instanceof PersonCustomer person) {
            return ResponseEntity.ok(toPersonCustomerResponse(person));
        } else if (customer instanceof CorporateCustomer corporate) {
            return ResponseEntity.ok(toCorporateCustomerResponse(corporate));
        }
        return ResponseEntity.notFound().build();
    }

    // ── Loans ─────────────────────────────────────────────────────────────────

    @PostMapping("/loans")
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request) {
        Loan loan = toLoan(request);
        salesEmployeUseCase.createLoan(loan, getAuthenticatedUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(toLoanResponse(loan));
    }

    @GetMapping("/loans/customer/{document}")
    public ResponseEntity<List<LoanResponse>> findLoansByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<LoanResponse> loans = salesEmployeUseCase.findLoansByCustomer(document, user)
                .stream().map(SalesEmployeController::toLoanResponse).toList();
        return ResponseEntity.ok(loans);
    }

    // ── Bank Accounts ─────────────────────────────────────────────────────────

    @PostMapping("/bank_accounts")
    public ResponseEntity<BankAccountResponse> createBankAccount(@Valid @RequestBody BankAccountRequest request) {
        BankAccount bankAccount = toBankAccount(request);
        salesEmployeUseCase.createBankAccount(bankAccount, getAuthenticatedUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(toBankAccountResponse(bankAccount));
    }

    @GetMapping("/bank_accounts/customer/{document}")
    public ResponseEntity<List<BankAccountResponse>> findBankAccountsByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<BankAccountResponse> accounts = salesEmployeUseCase.findBankAccountsByCustomer(document, user)
                .stream().map(SalesEmployeController::toBankAccountResponse).toList();
        return ResponseEntity.ok(accounts);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private static PersonCustomer toPersonCustomer(PersonCustomerRequest req) {
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

    private static CorporateCustomer toCorporateCustomer(CorporateCustomerRequest req) {
        CorporateCustomer corporateCustomer = new CorporateCustomer();
        corporateCustomer.setFullName(req.getFullName());
        corporateCustomer.setDocument(req.getDocument());
        corporateCustomer.setEmail(req.getEmail());
        corporateCustomer.setPhone(req.getPhone());
        corporateCustomer.setAddress(req.getAddress());
        corporateCustomer.setRolCustomer(req.getRolCustomer());
        corporateCustomer.setCustomerStatus(req.getCustomerStatus());
        if (req.getLegalRepresentative() != null) {
            corporateCustomer.setLegalRepresentative(toPersonCustomer(req.getLegalRepresentative()));
        }
        return corporateCustomer;
    }

    private static Loan toLoan(LoanRequest req) {
        Loan loan = new Loan();
        loan.setProductName(req.getProductName());
        loan.setProductCategory(req.getProductCategory());
        loan.setApproved(req.isApproved());
        loan.setLoanType(req.getLoanType());
        loan.setRequestedAmount(req.getRequestedAmount());
        loan.setInterestRate(req.getInterestRate());
        loan.setTermInMonths(req.getTermInMonths());
        loan.setLoanStatus(req.getLoanStatus());
        loan.setCreateDate(req.getCreateDate());
        loan.setApprovalDate(req.getApprovalDate());
        loan.setDisburseDate(req.getDisburseDate());
        loan.setDisburseAccount(req.getDisburseAccount());
        if (req.getCustomerOwner() != null) {
            PersonCustomer customer = new PersonCustomer();
            customer.setDocument(req.getCustomerOwner().getDocument());
            loan.setCustomerOwner(customer);
        }
        return loan;
    }

    private static BankAccount toBankAccount(BankAccountRequest req) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setProductName(req.getProductName());
        bankAccount.setProductCategory(req.getProductCategory());
        bankAccount.setAccountNumber(req.getAccountNumber());
        bankAccount.setCurrentBalance(req.getCurrentBalance());
        bankAccount.setAccountStatus(req.getAccountStatus());
        bankAccount.setOpeningDate(req.getOpeningDate());
        bankAccount.setAccountType(req.getAccountType());
        bankAccount.setCurrencyType(req.getCurrencyType());
        if (req.getCustomerOwner() != null) {
            PersonCustomer customer = new PersonCustomer();
            customer.setDocument(req.getCustomerOwner().getDocument());
            bankAccount.setCustomerOwner(customer);
        }
        return bankAccount;
    }

    private static PersonCustomerResponse toPersonCustomerResponse(PersonCustomer p) {
        return new PersonCustomerResponse(
                p.getId(), p.getFullName(), p.getDocument(),
                p.getEmail(), p.getPhone(), p.getAddress(),
                p.getRolCustomer(), p.getCustomerStatus(), p.getBirthDate());
    }

    private static CorporateCustomerResponse toCorporateCustomerResponse(CorporateCustomer c) {
        return new CorporateCustomerResponse(
                c.getId(), c.getFullName(), c.getDocument(),
                c.getEmail(), c.getPhone(), c.getAddress(),
                c.getRolCustomer(), c.getCustomerStatus(),
                toPersonCustomerResponse(c.getLegalRepresentative()));
    }

    private static CustomerResponse CustomerResponse(Customer c) {
        if (c instanceof PersonCustomer p) {
            return toPersonCustomerResponse(p);
        } else if (c instanceof CorporateCustomer co) {
            return toCorporateCustomerResponse(co);
        }
        throw new RuntimeException("Tipo no soportado");
    }

    private static LoanResponse toLoanResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(), loan.getProductName(), loan.getProductCategory(),
                loan.isApproved(), loan.getCustomerOwner(), loan.getLoanType(),
                loan.getCustomerOwner(), loan.getRequestedAmount(), loan.getApprovedAmount(),
                loan.getInterestRate(), loan.getTermInMonths(), loan.getLoanStatus(),
                loan.getCreateDate(), loan.getApprovalDate(),
                loan.getDisburseDate(), loan.getDisburseAccount());
    }

    private static BankAccountResponse toBankAccountResponse(BankAccount b) {
        return new BankAccountResponse(
                b.getId(), b.getProductName(), b.getProductCategory(),
                b.isApproved(), CustomerResponse(b.getCustomerOwner()), b.getAccountNumber(),
                b.getCurrentBalance(), b.getAccountStatus(), b.getOpeningDate(),
                b.getAccountType(), b.getCurrencyType());
    }
}