package app.application.adapters.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.adapters.api.response.BankAccountResponse;
import app.application.adapters.api.response.CustomerResponse;
import app.application.adapters.api.response.LoanResponse;
import app.application.adapters.api.response.PersonCustomerResponse;
import app.application.adapters.api.response.TransferResponse;
import app.application.usecases.CorporateSupervisorUseCase;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.ports.UserPort;

@RestController
@RequestMapping("/corporate_supervisor")
public class CorporateSupervisorController {

    @Autowired
    private final CorporateSupervisorUseCase corporateSupervisorUseCase;
    private final UserPort userPort;

    public CorporateSupervisorController(CorporateSupervisorUseCase corporateSupervisorUseCase, UserPort userPort) {
        this.corporateSupervisorUseCase = corporateSupervisorUseCase;
        this.userPort = userPort;
    }

    private User getAuthenticatedUser() {
        String document = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userPort.findByDocument(document);
    }

    // ── Transfers ─────────────────────────────────────────────────────────────
    @GetMapping("/transfers/account/{accountNumber}")
    public ResponseEntity<List<TransferResponse>> findTransfersByAccount(@PathVariable int accountNumber) {
        User user = getAuthenticatedUser();
        List<TransferResponse> transfers = corporateSupervisorUseCase.findTransfersByAccount(accountNumber, user)
                .stream().map(CorporateSupervisorController::toTransferResponse).toList();
        return ResponseEntity.ok(transfers);
    }

    @PutMapping("/transfers/{id}/approve")
    public ResponseEntity<Void> approveTransfer(@PathVariable UUID id) {
        corporateSupervisorUseCase.approveTransfer(id, getAuthenticatedUser());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/transfers/{id}/reject")
    public ResponseEntity<Void> rejectTransfer(@PathVariable UUID id) {
        corporateSupervisorUseCase.rejectTransfer(id, getAuthenticatedUser());
        return ResponseEntity.ok().build();
    }

    // ── Loans ─────────────────────────────────────────────────────────────────

    @GetMapping("/loans/customer/{document}")
    public ResponseEntity<List<LoanResponse>> findLoansByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<LoanResponse> loans = corporateSupervisorUseCase.findLoansByCustomer(document, user)
                .stream().map(CorporateSupervisorController::toLoanResponse).toList();
        return ResponseEntity.ok(loans);
    }

    // ── Bank Accounts ─────────────────────────────────────────────────────────



    @GetMapping("/bank_accounts/customer/{document}")
    public ResponseEntity<List<BankAccountResponse>> findBankAccountsByCustomer(@PathVariable String document) {
        User user = getAuthenticatedUser();
        List<BankAccountResponse> accounts = corporateSupervisorUseCase.findBankAccountsByCustomer(document, user)
                .stream().map(CorporateSupervisorController::toBankAccountResponse).toList();
        return ResponseEntity.ok(accounts);
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