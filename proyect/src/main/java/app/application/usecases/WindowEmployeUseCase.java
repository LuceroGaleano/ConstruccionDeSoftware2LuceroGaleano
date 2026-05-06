package app.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.Exception.NotFoundException;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.CreateBankAccount;
import app.domain.services.CreateCorporateCustomer;
import app.domain.services.CreatePersonCustomer;
import app.domain.services.CreateTransfer;
import app.domain.services.CreateUser;
import app.domain.services.FindBankAccount;
import app.domain.services.FindCustomer;
import app.domain.services.FindTransfer;
import app.domain.services.FindUser;

@Service
public class WindowEmployeUseCase {

    private final CreateTransfer createTransfer;
    private final FindTransfer findTransfer;
    private final CreateBankAccount createBankAccount;
    private final FindBankAccount findBankAccount;
    private final CreatePersonCustomer createPersonCustomer;
    private final CreateCorporateCustomer createCorporateCustomer;
    private final FindCustomer findCustomer;
    private final CreateUser createUser;
    private final FindUser findUser;

    public WindowEmployeUseCase(
            CreateTransfer createTransfer,
            FindTransfer findTransfer,
            CreateBankAccount createBankAccount,
            FindBankAccount findBankAccount,
            CreatePersonCustomer createPersonCustomer,
            CreateCorporateCustomer createCorporateCustomer,
            FindCustomer findCustomer,
            CreateUser createUser,
            FindUser findUser) {
        this.createTransfer = createTransfer;
        this.findTransfer = findTransfer;
        this.createBankAccount = createBankAccount;
        this.findBankAccount = findBankAccount;
        this.createPersonCustomer = createPersonCustomer;
        this.createCorporateCustomer = createCorporateCustomer;
        this.findCustomer = findCustomer;
        this.createUser = createUser;
        this.findUser = findUser;
    }

    // ── Transfers ──────────────────────────────────────────────────────────────

    public void createTransfer(Transfer transfer, User user) throws BussinesException {
        createTransfer.createTransfer(transfer, user);
    }

    public Transfer findTransferById(UUID id) throws NotFoundException {
        return findTransfer.findById(id);
    }

    public List<Transfer> findTransfersByAccount(UUID accountId) throws NotFoundException {
        return findTransfer.findByAccount(accountId);
    }

    // ── Bank Accounts ──────────────────────────────────────────────────────────

    public void createBankAccount(BankAccount bankAccount, User user) throws BussinesException {
        createBankAccount.createBankAccount(bankAccount, user);
    }

    public BankAccount findBankAccountById(UUID id) throws NotFoundException {
        return findBankAccount.findById(id);
    }

    public BankAccount findBankAccountByNumber(int accountNumber) throws NotFoundException {
        return findBankAccount.findByAccountNumber(accountNumber);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document) throws NotFoundException {
        return findBankAccount.findByCustomerOwner(document);
    }

    // ── Customers ──────────────────────────────────────────────────────────────

    public void createPersonCustomer(PersonCustomer personCustomer) throws BussinesException {
        createPersonCustomer.createPersonCustomer(personCustomer);
    }

    public void createCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException {
        createCorporateCustomer.createCorporateCustomer(corporateCustomer);
    }

    public Customer findCustomerByDocument(String document) throws NotFoundException {
        return findCustomer.findCustomer(document);
    }

    // ── Users ──────────────────────────────────────────────────────────────────

    public void createUser(User user) throws BussinesException {
        createUser.createUser(user);
    }

    public User findUserByDocument(String document) throws NotFoundException {
        return findUser.findByDocument(document);
    }
}