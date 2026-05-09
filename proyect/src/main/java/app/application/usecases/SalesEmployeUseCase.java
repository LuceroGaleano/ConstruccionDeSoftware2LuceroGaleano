package app.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.User;
import app.domain.services.CreateBankAccount;
import app.domain.services.CreateCorporateCustomer;
import app.domain.services.CreateLoan;
import app.domain.services.CreatePersonCustomer;
import app.domain.services.FindBankAccount;
import app.domain.services.FindCustomer;
import app.domain.services.FindLoan;

@Service
public class SalesEmployeUseCase {

    private final CreatePersonCustomer createPersonCustomer;
    private final CreateCorporateCustomer createCorporateCustomer;
    private final FindCustomer findCustomer;
    private final CreateLoan createLoan;
    private final FindLoan findLoan;
    private final CreateBankAccount createBankAccount;
    private final FindBankAccount findBankAccount;

    public SalesEmployeUseCase(
            CreatePersonCustomer createPersonCustomer,
            CreateCorporateCustomer createCorporateCustomer,
            FindCustomer findCustomer,
            CreateLoan createLoan,
            FindLoan findLoan,
            CreateBankAccount createBankAccount,
            FindBankAccount findBankAccount) {
        this.createPersonCustomer = createPersonCustomer;
        this.createCorporateCustomer = createCorporateCustomer;
        this.findCustomer = findCustomer;
        this.createLoan = createLoan;
        this.findLoan = findLoan;
        this.createBankAccount = createBankAccount;
        this.findBankAccount = findBankAccount;
    }

    public void createPersonCustomer(PersonCustomer personCustomer) throws BussinesException {
        createPersonCustomer.createPersonCustomer(personCustomer);
    }

    public void createCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException {
        createCorporateCustomer.createCorporateCustomer(corporateCustomer);
    }

    public Customer findCustomerByDocument(String document) throws BussinesException {
        return findCustomer.findCustomer(document);
    }

    public void createLoan(Loan loan, User user) throws BussinesException {
        createLoan.createLoan(loan, user);
    }

    public List<Loan> findLoansByCustomer(String document) throws BussinesException {
        return findLoan.findByCustomer(document);
    }

    public void createBankAccount(BankAccount bankAccount, User user) throws BussinesException {
        createBankAccount.createBankAccount(bankAccount, user);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document);
    }
}