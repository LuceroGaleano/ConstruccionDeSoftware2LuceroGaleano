package app.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.ApproveTransfer;
import app.domain.services.FindBankAccount;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;
import app.domain.services.RejectTransfer;

@Service
public class CorporateSupervisorUseCase {

    private final FindTransfer findTransfer;
    private final FindLoan findLoan;
    private final FindBankAccount findBankAccount;
    private final ApproveTransfer approveTransfer;
    private final RejectTransfer rejectTransfer;

    public CorporateSupervisorUseCase(
            FindTransfer findTransfer,
            FindLoan findLoan,
            FindBankAccount findBankAccount,
            ApproveTransfer approveTransfer,
            RejectTransfer rejectTransfer) {
        this.findTransfer = findTransfer;
        this.findLoan = findLoan;
        this.findBankAccount = findBankAccount;
        this.approveTransfer = approveTransfer;
        this.rejectTransfer = rejectTransfer;
    }

    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws BussinesException {
        return findTransfer.findByAccount(accountNumber, user);
    }


    public List<Loan> findLoansByCustomer(String document, User user) throws BussinesException {
        return findLoan.findByCustomer(document, user);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document, User user) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document, user);
    }

    public void approveTransfer(UUID id, User user) throws BussinesException {
        approveTransfer.approveTransfer(id, user);
    }

    public void rejectTransfer(UUID id, User user) throws BussinesException {
        rejectTransfer.rejectTransfer(id, user);
    }
}