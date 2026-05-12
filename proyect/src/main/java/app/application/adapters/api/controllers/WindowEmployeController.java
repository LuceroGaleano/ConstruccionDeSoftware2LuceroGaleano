package app.application.adapters.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.request.BankAccountRequest;
import app.application.adapters.api.request.CorporateCustomerRequest;
import app.application.adapters.api.request.CustomerRequest;
import app.application.adapters.api.request.OnCreate;
import app.application.adapters.api.request.OnSearch;
import app.application.adapters.api.request.PersonCustomerRequest;
import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.api.request.UserRequest;
import app.application.adapters.api.response.BankAccountResponse;
import app.application.adapters.api.response.CorporateCustomerResponse;
import app.application.adapters.api.response.CustomerResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.adapters.api.response.TransferResponse;
import app.application.adapters.api.response.UserResponse;
import app.application.usecases.WindowEmployeUseCase;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.RolUser;
import app.domain.ports.UserPort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/window_employe")
public class WindowEmployeController {

    @Autowired
    private final WindowEmployeUseCase windowEmployeUseCase;
    private final UserPort userPort;

    public WindowEmployeController(WindowEmployeUseCase windowEmployeUseCase, UserPort userPort) {
        this.windowEmployeUseCase = windowEmployeUseCase;
        this.userPort = userPort;
    }

    private User getAuthenticatedUser() {
        String document = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userPort.findByDocument(document);
    }

    // ── Transfers ─────────────────────────────────────────────────────────────

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        Transfer transfer = toTransfer(request);
        windowEmployeUseCase.createTransfer(transfer, getAuthenticatedUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(toTransferResponse(transfer));
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<TransferResponse> findTransferById(@PathVariable UUID id) {
        Transfer transfer = windowEmployeUseCase.findTransferById(id);
        return ResponseEntity.ok(toTransferResponse(transfer));
    }

    @GetMapping("/transfers/account/{accountNumber}")
    public ResponseEntity<List<TransferResponse>> findTransfersByAccount(@PathVariable int accountNumber) {
        User user = getAuthenticatedUser();
        List<TransferResponse> transfers = windowEmployeUseCase.findTransfersByAccount(accountNumber, user)
                .stream().map(WindowEmployeController::toTransferResponse).toList();
        return ResponseEntity.ok(transfers);
    }

    // ── Bank Accounts ─────────────────────────────────────────────────────────

    @PostMapping("/bank_accounts")
    public ResponseEntity<BankAccountResponse> createBankAccount(@Valid @RequestBody BankAccountRequest request) {
        BankAccount bankAccount = toBankAccount(request);
        windowEmployeUseCase.createBankAccount(bankAccount, getAuthenticatedUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(toBankAccountResponse(bankAccount));
    }

    @GetMapping("/bank_accounts/{id}")
    public ResponseEntity<BankAccountResponse> findBankAccountById(@PathVariable UUID id) {
        BankAccount bankAccount = windowEmployeUseCase.findBankAccountById(id);
        return ResponseEntity.ok(toBankAccountResponse(bankAccount));
    }

    @GetMapping("/bank_accounts/number/{accountNumber}")
    public ResponseEntity<BankAccountResponse> findBankAccountByNumber(@PathVariable int accountNumber) {
        User user = getAuthenticatedUser();
        BankAccount bankAccount = windowEmployeUseCase.findBankAccountByNumber(accountNumber, user);
        return ResponseEntity.ok(toBankAccountResponse(bankAccount));
    }

    @GetMapping("/bank_accounts/customer/{document}")
    public ResponseEntity<List<BankAccountResponse>> findBankAccountsByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<BankAccountResponse> accounts = windowEmployeUseCase.findBankAccountsByCustomer(document, user)
                .stream().map(WindowEmployeController::toBankAccountResponse).toList();
        return ResponseEntity.ok(accounts);
    }

    // ── Customers ─────────────────────────────────────────────────────────────

    @PostMapping("/person_customer")
    public ResponseEntity<PersonCustomerResponse> createPersonCustomer(
            @Validated(OnCreate.class) @RequestBody PersonCustomerRequest request) {
        PersonCustomer personCustomer = toPersonCustomer(request);
        windowEmployeUseCase.createPersonCustomer(personCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toPersonCustomerResponse(personCustomer));
    }

    @PutMapping("/person_customer/{document}")
    public ResponseEntity<PersonCustomerResponse> updatePersonCustomer(
            @PathVariable String document,
            @Validated(OnSearch.class) @RequestBody PersonCustomerRequest request) {
        PersonCustomer personCustomer = toPersonCustomer(request);
        personCustomer.setDocument(document);
        windowEmployeUseCase.updatePersonCustomer(personCustomer);
        return ResponseEntity.ok(toPersonCustomerResponse(personCustomer));
    }

    @PostMapping("/corporate_customer")
    public ResponseEntity<CorporateCustomerResponse> createCorporateCustomer(
            @Validated(OnCreate.class) @RequestBody CorporateCustomerRequest request) {
        CorporateCustomer corporateCustomer = toCorporateCustomer(request);
        windowEmployeUseCase.createCorporateCustomer(corporateCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toCorporateCustomerResponse(corporateCustomer));
    }

    @PutMapping("/corporate_customer/{document}")
    public ResponseEntity<CorporateCustomerResponse> updateCorporateCustomer(
            @PathVariable String document,
            @Validated(OnSearch.class) @RequestBody CorporateCustomerRequest request) {
        CorporateCustomer corporateCustomer = toCorporateCustomer(request);
        corporateCustomer.setDocument(document);
        windowEmployeUseCase.updateCorporateCustomer(corporateCustomer);
        CorporateCustomer updated = (CorporateCustomer) windowEmployeUseCase.findCustomerByDocument(document);
        return ResponseEntity.ok(toCorporateCustomerResponse(updated));
    }

    @GetMapping("/customer/{document}")
    public ResponseEntity<?> findCustomerByDocument(@PathVariable String document) {
        Customer customer = windowEmployeUseCase.findCustomerByDocument(document);
        if (customer instanceof PersonCustomer person) {
            return ResponseEntity.ok(toPersonCustomerResponse(person));
        } else if (customer instanceof CorporateCustomer corporate) {
            return ResponseEntity.ok(toCorporateCustomerResponse(corporate));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/customer/{document}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String document) {
        windowEmployeUseCase.deleteCustomer(document);
        return ResponseEntity.noContent().build();
    }

    // ── Users ─────────────────────────────────────────────────────────────────

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Validated(OnCreate.class) @RequestBody UserRequest request) {
        User user = toUser(request);
        windowEmployeUseCase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toUserResponse(user));
    }

    @GetMapping("/users/{document}")
    public ResponseEntity<UserResponse> findUserByDocument(@PathVariable String document) {
        User user = windowEmployeUseCase.findUserByDocument(document);
        return ResponseEntity.ok(toUserResponse(user));
    }

    @PutMapping("/users/{document}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String document,
            @Validated(OnSearch.class) @RequestBody UserRequest request) {
        User user = toUser(request);
        user.setDocument(document);
        windowEmployeUseCase.updateUser(user);
        return ResponseEntity.ok(toUserResponse(user));
    }

    @DeleteMapping("/users/{document}")
    public ResponseEntity<Void> deleteUser(@PathVariable String document) {
        windowEmployeUseCase.deleteUser(document);
        return ResponseEntity.noContent().build();
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private static Transfer toTransfer(TransferRequest req) {
        Transfer transfer = new Transfer();
        transfer.setIdCreator(req.getIdCreator());
        transfer.setIdApprover(req.getIdApprover());
        transfer.setAmount(req.getAmount());
        BankAccount origin = new BankAccount();
        origin.setAccountNumber(req.getOriginAccountNumber());
        transfer.setOriginAccount(origin);
        BankAccount destination = new BankAccount();
        destination.setAccountNumber(req.getDestinationAccountNumber());
        transfer.setDestinationAccount(destination);
        return transfer;
    }

    private static User toUser(UserRequest req) {
        User user = new User();
        user.setFullName(req.getFullName());
        user.setDocument(req.getDocument());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setBirthDate(req.getBirthDate());
        user.setSystemRole(req.getSystemRole());
        user.setUserStatus(req.getUserStatus());
        user.setUserName(req.getUserName());
        user.setPassword(req.getPassword());
        user.setCompany(req.getCompany());
        if (req.getCustomer() != null) {
            if (req.getSystemRole() == RolUser.CorporateCustomerUser) {
                user.setCustomer(toCorporateCustomer(req.getCustomer()));
            } else {
                user.setCustomer(toPersonCustomer(req.getCustomer()));
            }
        }
        return user;
    }

    // Mapper desde CustomerRequest (para toUser)
    private static PersonCustomer toPersonCustomer(CustomerRequest req) {
        PersonCustomer personCustomer = new PersonCustomer();
        personCustomer.setDocument(req.getDocument());
        personCustomer.setFullName(req.getFullName());
        personCustomer.setEmail(req.getEmail());
        personCustomer.setPhone(req.getPhone());
        personCustomer.setAddress(req.getAddress());
        personCustomer.setBirthDate(req.getBirthDate());
        return personCustomer;
    }

    // Mapper desde CustomerRequest (para toUser)
    private static CorporateCustomer toCorporateCustomer(CustomerRequest req) {
        CorporateCustomer corporateCustomer = new CorporateCustomer();
        corporateCustomer.setDocument(req.getDocument());
        corporateCustomer.setFullName(req.getFullName());
        corporateCustomer.setEmail(req.getEmail());
        corporateCustomer.setPhone(req.getPhone());
        corporateCustomer.setAddress(req.getAddress());
        if (req.getLegalRepresentative() != null) {
            corporateCustomer.setLegalRepresentative(toPersonCustomer(req.getLegalRepresentative()));
        }
        return corporateCustomer;
    }

    // Mapper desde PersonCustomerRequest (para endpoints de customer)
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

    // Mapper desde CorporateCustomerRequest (para endpoints de customer)
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

    private static CustomerResponse customerResponse(Customer c) {
        if (c instanceof PersonCustomer p) {
            return toPersonCustomerResponse(p);
        } else if (c instanceof CorporateCustomer co) {
            return toCorporateCustomerResponse(co);
        }
        throw new RuntimeException("Tipo no soportado");
    }

    private static TransferResponse toTransferResponse(Transfer t) {
        return new TransferResponse(
                t.getId(), t.getOriginAccount(), t.getDestinationAccount(),
                t.getAmount(), t.getCreationDate(), t.getApprovalDate(),
                t.getTransferStatus(), t.getIdCreator(), t.getIdApprover());
    }

    private static BankAccountResponse toBankAccountResponse(BankAccount b) {
        return new BankAccountResponse(
                b.getId(), b.getProductName(), b.getProductCategory(),
                b.isApproved(), customerResponse(b.getCustomerOwner()), b.getAccountNumber(),
                b.getCurrentBalance(), b.getAccountStatus(), b.getOpeningDate(),
                b.getAccountType(), b.getCurrencyType());
    }

    private static UserResponse toUserResponse(User u) {
        return new UserResponse(
                u.getFullName(), u.getDocument(), u.getEmail(),
                u.getPhone(), u.getAddress(), u.getUserID(), u.getBirthDate(), u.getSystemRole(),
                u.getUserStatus(), u.getUserName(), u.getPassword(),
                u.getCompany(), null);
    }
}