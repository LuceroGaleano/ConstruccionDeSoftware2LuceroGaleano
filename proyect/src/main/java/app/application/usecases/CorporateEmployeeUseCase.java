package app.application.usecases;

import java.util.List;

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

    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws BussinesException {
        return findTransfer.findByAccount(accountNumber, user);
    }

    public List<Loan> findLoansByCustomer(String document, User user) throws BussinesException {
        return findLoan.findByCustomer(document, user);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document, User user) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document, user);
    }
}