package app.application.adapters.api.controllers;

import java.util.List;

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

import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.api.response.BankAccountResponse;
import app.application.adapters.api.response.CustomerResponse;
import app.application.adapters.api.response.LoanResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.adapters.api.response.TransferResponse;
import app.application.usecases.CorporateEmployeeUseCase;
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
@RequestMapping("/corporate_employe")
public class CorporateEmployeeController {

    @Autowired
    private final CorporateEmployeeUseCase corporateEmployeeUseCase;
    private final UserPort userPort;

    public CorporateEmployeeController(CorporateEmployeeUseCase corporateEmployeeUseCase, UserPort userPort) {
        this.corporateEmployeeUseCase = corporateEmployeeUseCase;
        this.userPort = userPort;
    }

    private User getAuthenticatedUser() {
        String document = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userPort.findByDocument(document);
    }

    // ── Transfers ─────────────────────────────────────────────────────────────

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        User user = getAuthenticatedUser();
        Transfer transfer = toTransfer(request);
        corporateEmployeeUseCase.createTransfer(user, transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toTransferResponse(transfer));
    }


    @GetMapping("/transfers/account/{accountNumber}")
    public ResponseEntity<List<TransferResponse>> findTransfersByAccount(@PathVariable int accountNumber) {
        User user = getAuthenticatedUser();
        List<TransferResponse> transfers = corporateEmployeeUseCase.findTransfersByAccount(accountNumber, user)
                .stream().map(CorporateEmployeeController::toTransferResponse).toList();
        return ResponseEntity.ok(transfers);
    }

    // ── Loans ─────────────────────────────────────────────────────────────────


    @GetMapping("/loans/customer/{document}")
    public ResponseEntity<List<LoanResponse>> findLoansByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<LoanResponse> loans = corporateEmployeeUseCase.findLoansByCustomer(document, user)
                .stream().map(CorporateEmployeeController::toLoanResponse).toList();
        return ResponseEntity.ok(loans);
    }

    // ── Bank Accounts ─────────────────────────────────────────────────────────


    @GetMapping("/bank_accounts/customer/{document}")
    public ResponseEntity<List<BankAccountResponse>> findBankAccountsByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<BankAccountResponse> accounts = corporateEmployeeUseCase.findBankAccountsByCustomer(document, user)
                .stream().map(CorporateEmployeeController::toBankAccountResponse).toList();
        return ResponseEntity.ok(accounts);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private static Transfer toTransfer(TransferRequest req) {
        Transfer transfer = new Transfer();
        transfer.setAmount(req.getAmount());
        transfer.setIdApprover(req.getIdApprover());
        BankAccount origin = new BankAccount();
        origin.setAccountNumber(req.getOriginAccountNumber());
        transfer.setOriginAccount(origin);
        BankAccount destination = new BankAccount();
        destination.setAccountNumber(req.getDestinationAccountNumber());
        transfer.setDestinationAccount(destination);
        return transfer;
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