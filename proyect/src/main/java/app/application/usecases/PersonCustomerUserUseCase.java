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
import app.domain.services.CreateTransfer;
import app.domain.services.FindBankAccount;
import app.domain.services.FindLoan;

@Service
public class PersonCustomerUserUseCase {
    private final CreateLoan createLoan;
    private final FindLoan findLoan;
    private final CreateTransfer createTransfer;
    private final FindBankAccount findBankAccount;

    public PersonCustomerUserUseCase(CreateLoan createLoan, FindLoan findLoan, CreateTransfer createTransfer, FindBankAccount findBankAccount){
        this.createLoan = createLoan;
        this.findLoan = findLoan;
        this.createTransfer = createTransfer;
        this.findBankAccount = findBankAccount;
    }

    public void createLoan(User user, Loan loan) throws BussinesException {
        loan.setCustomerOwner(user.getCustomer());
        createLoan.createLoan(loan, user);
    }

    public Loan findByIdLoan(UUID id) throws BussinesException{
        return findLoan.findById(id);
    }

    public List<Loan> findMyLoans(User user) throws BussinesException{
        return findLoan.findMyLoans(user);
    }

    public void createTransfer(User user, Transfer transfer) throws BussinesException {
        transfer.setIdCreator(user.getDocument());
        createTransfer.createTransfer(transfer, user);
    }

    public BankAccount findAccountById(UUID id) throws BussinesException{
        return findBankAccount.findById(id);
    }

    public List<BankAccount> findAccountByCustomer(String customerDocument) throws BussinesException{
        return findBankAccount.findByCustomerOwner(customerDocument);
    }
}