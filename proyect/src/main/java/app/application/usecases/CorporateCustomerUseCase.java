package app.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.CreateLoan;
import app.domain.services.FindBankAccount;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;

@Service
public class CorporateCustomerUseCase {

    private final CreateLoan createLoan;
    private final FindLoan findLoan;
    private final FindTransfer findTransfer;
    private final FindBankAccount findBankAccount;

    public CorporateCustomerUseCase(
            CreateLoan createLoan,
            FindLoan findLoan,
            FindTransfer findTransfer,
            FindBankAccount findBankAccount) {
        this.createLoan = createLoan;
        this.findLoan = findLoan;
        this.findTransfer = findTransfer;
        this.findBankAccount = findBankAccount;
    }

    public void createLoan(User user, Loan loan) throws BussinesException {
        createLoan.createLoan(loan, user);
    }


    public List<Loan> findMyLoans(User user) throws BussinesException {
        return findLoan.findMyLoans(user);
    }


    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws BussinesException {
        return findTransfer.findByAccount(accountNumber, user);
    }


    public List<BankAccount> findBankAccountsByCustomer(String document) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document);
    }
}