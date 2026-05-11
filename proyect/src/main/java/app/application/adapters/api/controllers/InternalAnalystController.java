package app.application.adapters.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.request.ApproveLoanRequest;
import app.application.adapters.api.response.BankAccountResponse;
import app.application.adapters.api.response.CorporateCustomerResponse;
import app.application.adapters.api.response.CustomerResponse;
import app.application.adapters.api.response.LoanResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.adapters.api.response.TransferResponse;
import app.application.usecases.InternalAnalystUseCase;
import app.domain.models.BankAccount;
import app.domain.models.Bitacora;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.ports.UserPort;

@RestController
@RequestMapping("/internal_analyst")
public class InternalAnalystController {

    @Autowired
    private final InternalAnalystUseCase internalAnalystUseCase;
    private final UserPort userPort;

    public InternalAnalystController(InternalAnalystUseCase internalAnalystUseCase, UserPort userPort) {
        this.internalAnalystUseCase = internalAnalystUseCase;
        this.userPort = userPort;
    }

    private User getAuthenticatedUser() {
        String document = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userPort.findByDocument(document);
    }

    // ── Customers ─────────────────────────────────────────────────────────────

    @GetMapping("/customer/{document}")
    public ResponseEntity<?> findCustomerByDocument(@PathVariable String document) {
        Customer customer = internalAnalystUseCase.findCustomerByDocument(document);
        if (customer instanceof PersonCustomer person) {
            return ResponseEntity.ok(toPersonCustomerResponse(person));
        } else if (customer instanceof CorporateCustomer corporate) {
            return ResponseEntity.ok(toCorporateCustomerResponse(corporate));
        }
        return ResponseEntity.notFound().build();
    }

    // ── Loans ─────────────────────────────────────────────────────────────────

    @GetMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> findLoanById(@PathVariable UUID id) {
        Loan loan = internalAnalystUseCase.findLoanById(id);
        return ResponseEntity.ok(toLoanResponse(loan));
    }

    @GetMapping("/loans/customer/{document}")
    public ResponseEntity<List<LoanResponse>> findLoansByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<LoanResponse> loans = internalAnalystUseCase.findLoansByCustomer(document, user)
                .stream().map(InternalAnalystController::toLoanResponse).toList();
        return ResponseEntity.ok(loans);
    }

    @PutMapping("/loans/{id}/approve")
    public ResponseEntity<Void> approveLoan(
            @PathVariable UUID id,
            @RequestBody ApproveLoanRequest request) {
        internalAnalystUseCase.approveLoan(id, getAuthenticatedUser(), request.getApprovedAmount());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/loans/{id}/reject")
    public ResponseEntity<Void> rejectLoan(@PathVariable UUID id) {
        internalAnalystUseCase.rejectLoan(id, getAuthenticatedUser());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/loans/{id}/disburse")
    public ResponseEntity<Void> disburseLoan(@PathVariable UUID id) {
        User user = getAuthenticatedUser();
        internalAnalystUseCase.disburseLoan(id, user);
        return ResponseEntity.ok().build();
    }

    // ── Transfers ─────────────────────────────────────────────────────────────

    @GetMapping("/transfers/{id}")
    public ResponseEntity<TransferResponse> findTransferById(@PathVariable UUID id) {
        Transfer transfer = internalAnalystUseCase.findTransferById(id);
        return ResponseEntity.ok(toTransferResponse(transfer));
    }

    @GetMapping("/transfers/account/{accountNumber}")
    public ResponseEntity<List<TransferResponse>> findTransfersByAccount(@PathVariable int accountNumber) {
        User user = getAuthenticatedUser();
        List<TransferResponse> transfers = internalAnalystUseCase.findTransfersByAccount(accountNumber, user)
                .stream().map(InternalAnalystController::toTransferResponse).toList();
        return ResponseEntity.ok(transfers);
    }

    // ── Bank Accounts ─────────────────────────────────────────────────────────

    @GetMapping("/bank_accounts/{id}")
    public ResponseEntity<BankAccountResponse> findBankAccountById(@PathVariable UUID id) {
        BankAccount account = internalAnalystUseCase.findBankAccountById(id);
        return ResponseEntity.ok(toBankAccountResponse(account));
    }

    @GetMapping("/bank_accounts/customer/{document}")
    public ResponseEntity<List<BankAccountResponse>> findBankAccountsByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<BankAccountResponse> accounts = internalAnalystUseCase.findBankAccountsByCustomer(document, user)
                .stream().map(InternalAnalystController::toBankAccountResponse).toList();
        return ResponseEntity.ok(accounts);
    }

    // ── Bitacora ──────────────────────────────────────────────────────────────

    @GetMapping("/bitacora/{id}")
    public ResponseEntity<Bitacora> findBitacoraById(@PathVariable String id) {
        Bitacora bitacora = internalAnalystUseCase.findBitacoraById(id);
        return ResponseEntity.ok(bitacora);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private static LoanResponse toLoanResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(), loan.getProductName(), loan.getProductCategory(),
                loan.isApproved(), loan.getCustomerOwner(), loan.getLoanType(),
                loan.getCustomerOwner(), loan.getRequestedAmount(), loan.getApprovedAmount(),
                loan.getInterestRate(), loan.getTermInMonths(), loan.getLoanStatus(),
                loan.getCreateDate(), loan.getApprovalDate(),
                loan.getDisburseDate(), loan.getDisburseAccount());
    }

    private static TransferResponse toTransferResponse(Transfer t) {
        return new TransferResponse(
                t.getId(), t.getOriginAccount(), t.getDestinationAccount(),
                t.getAmount(), t.getCreationDate(), t.getApprovalDate(),
                t.getTransferStatus(), t.getIdCreator(), t.getIdApprover());
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

    private static BankAccountResponse toBankAccountResponse(BankAccount b) {
        return new BankAccountResponse(
                b.getId(), b.getProductName(), b.getProductCategory(),
                b.isApproved(), customerResponse(b.getCustomerOwner()), b.getAccountNumber(),
                b.getCurrentBalance(), b.getAccountStatus(), b.getOpeningDate(),
                b.getAccountType(), b.getCurrencyType());
    }
}