package app.application.adapters.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.request.LoanRequest;
import app.application.adapters.api.response.BankAccountResponse;
import app.application.adapters.api.response.CustomerResponse;
import app.application.adapters.api.response.LoanResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.adapters.api.response.TransferResponse;
import app.application.usecases.CorporateCustomerUseCase;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.ports.UserPort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/corporate_customer_user")
public class CorporateCustomerController {

    @Autowired
    private final CorporateCustomerUseCase corporateCustomerUseCase;
    private final UserPort userPort;

    public CorporateCustomerController(CorporateCustomerUseCase corporateCustomerUseCase, UserPort userPort) {
        this.corporateCustomerUseCase = corporateCustomerUseCase;
        this.userPort = userPort;
    }

    private User getAuthenticatedUser() {
        String document = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userPort.findByDocument(document);
    }

    // ── Loans ─────────────────────────────────────────────────────────────────

    @PostMapping("/loans")
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request) {
        User user = getAuthenticatedUser();
        Loan loan = toLoan(request);
        corporateCustomerUseCase.createLoan(user, loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(toLoanResponse(loan));
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanResponse>> findMyLoans() {
        User user = getAuthenticatedUser();
        List<LoanResponse> loans = corporateCustomerUseCase.findMyLoans(user)
                .stream().map(CorporateCustomerController::toLoanResponse).toList();
        return ResponseEntity.ok(loans);
    }

    // ── Transfers ─────────────────────────────────────────────────────────────

    @GetMapping("/transfers/account/{accountNumber}")
    public ResponseEntity<List<TransferResponse>> findTransfersByAccount(@PathVariable int accountNumber) {
        User user = getAuthenticatedUser();
        List<TransferResponse> transfers = corporateCustomerUseCase.findTransfersByAccount(accountNumber, user)
                .stream().map(CorporateCustomerController::toTransferResponse).toList();
        return ResponseEntity.ok(transfers);
    }

    // ── Bank Accounts ─────────────────────────────────────────────────────────


    @GetMapping("/bank_accounts")
    public ResponseEntity<List<BankAccountResponse>> findMyAccounts() {
        User user = getAuthenticatedUser();
        List<BankAccountResponse> accounts = corporateCustomerUseCase
                .findBankAccountsByCustomer(user.getDocument())
                .stream().map(CorporateCustomerController::toBankAccountResponse).toList();
        return ResponseEntity.ok(accounts);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private static Loan toLoan(LoanRequest req) {
        Loan loan = new Loan();
        loan.setLoanType(req.getLoanType());
        loan.setRequestedAmount(req.getRequestedAmount());
        loan.setInterestRate(req.getInterestRate());
        loan.setTermInMonths(req.getTermInMonths());
        loan.setDisburseAccount(req.getDisburseAccount());
        if (req.getCustomerOwner() != null) {
            CorporateCustomer customer = new CorporateCustomer();
            customer.setDocument(req.getCustomerOwner().getDocument());
            loan.setCustomerOwner(customer);
        }
        return loan;
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

    private static TransferResponse toTransferResponse(Transfer t) {
        return new TransferResponse(
                t.getId(), t.getOriginAccount(), t.getDestinationAccount(),
                t.getAmount(), t.getCreationDate(), t.getApprovalDate(),
                t.getTransferStatus(), t.getIdCreator(), t.getIdApprover());
    }

    private static CustomerResponse customerResponse(Customer c) {
        if (c instanceof PersonCustomer p) {
            return new PersonCustomerResponse(
                    p.getId(), p.getFullName(), p.getDocument(),
                    p.getEmail(), p.getPhone(), p.getAddress(),
                    p.getRolCustomer(), p.getCustomerStatus(), p.getBirthDate());
        } else if (c instanceof CorporateCustomer co) {
            return new PersonCustomerResponse(
                    co.getId(), co.getFullName(), co.getDocument(),
                    co.getEmail(), co.getPhone(), co.getAddress(),
                    co.getRolCustomer(), co.getCustomerStatus(), null);
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