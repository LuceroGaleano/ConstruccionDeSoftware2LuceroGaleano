package app.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.CreateTransfer;
import app.domain.services.FindBankAccount;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;

@Service
public class CorporateEmployeeUseCase {

    private final CreateTransfer createTransfer;
    private final FindTransfer findTransfer;
    private final FindLoan findLoan;
    private final FindBankAccount findBankAccount;

    public CorporateEmployeeUseCase(
            CreateTransfer createTransfer,
            FindTransfer findTransfer,
            FindLoan findLoan,
            FindBankAccount findBankAccount) {
        this.createTransfer = createTransfer;
        this.findTransfer = findTransfer;
        this.findLoan = findLoan;
        this.findBankAccount = findBankAccount;
    }

    public void createTransfer(User user, Transfer transfer) throws BussinesException {
        transfer.setIdCreator(user.getDocument());
        createTransfer.createTransfer(transfer, user);
    }

    public Transfer findTransferById(UUID id) throws BussinesException {
        return findTransfer.findById(id);
    }

    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws BussinesException {
        return findTransfer.findByAccount(accountNumber, user);
    }

    public Loan findLoanById(UUID id) throws BussinesException {
        return findLoan.findById(id);
    }

    public List<Loan> findLoansByCustomer(String document) throws BussinesException {
        return findLoan.findByCustomer(document);
    }

    public BankAccount findBankAccountById(UUID id) throws BussinesException {
        return findBankAccount.findById(id);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document);
    }
}