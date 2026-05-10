package app.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.CreateLoan;
import app.domain.services.CreateTransfer;
import app.domain.services.FindBankAccount;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;

@Service
public class PersonCustomerUserUseCase {

    private final CreateLoan createLoan;
    private final FindLoan findLoan;
    private final CreateTransfer createTransfer;
    private final FindTransfer findTransfer;
    private final FindBankAccount findBankAccount;

    public PersonCustomerUserUseCase(
            CreateLoan createLoan,
            FindLoan findLoan,
            CreateTransfer createTransfer,
            FindTransfer findTransfer,
            FindBankAccount findBankAccount) {
        this.createLoan = createLoan;
        this.findLoan = findLoan;
        this.createTransfer = createTransfer;
        this.findTransfer = findTransfer;
        this.findBankAccount = findBankAccount;
    }

    public void createLoan(User user, Loan loan) throws BussinesException {
        createLoan.createLoan(loan, user);
    }

    public List<Loan> findMyLoans(User user) throws BussinesException {
        return findLoan.findMyLoans(user);
    }

    public void createTransfer(User user, Transfer transfer) throws BussinesException {
        transfer.setIdCreator(user.getDocument());
        createTransfer.createTransfer(transfer, user);
    }


    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws BussinesException {
        return findTransfer.findByAccount(accountNumber, user);
    }


    public List<BankAccount> findAccountByCustomer(String document, User user) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document, user);
    }
}