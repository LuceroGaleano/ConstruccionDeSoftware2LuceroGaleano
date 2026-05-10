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
import app.domain.services.DeleteCustomer;
import app.domain.services.DeleteUser;
import app.domain.services.FindBankAccount;
import app.domain.services.FindCustomer;
import app.domain.services.FindTransfer;
import app.domain.services.FindUser;
import app.domain.services.UpdateCorporateCustomer;
import app.domain.services.UpdatePersonCustomer;
import app.domain.services.UpdateUser;

@Service
public class WindowEmployeUseCase {

    private final CreateTransfer createTransfer;
    private final FindTransfer findTransfer;
    private final CreateBankAccount createBankAccount;
    private final FindBankAccount findBankAccount;
    private final CreatePersonCustomer createPersonCustomer;
    private final CreateCorporateCustomer createCorporateCustomer;
    private final FindCustomer findCustomer;
    private final DeleteCustomer deleteCustomer;
    private final UpdatePersonCustomer updatePersonCustomer;
    private final UpdateCorporateCustomer updateCorporateCustomer;
    private final CreateUser createUser;
    private final FindUser findUser;
    private final DeleteUser deleteUser;
    private final UpdateUser updateUser;

    public WindowEmployeUseCase(
            CreateTransfer createTransfer,
            FindTransfer findTransfer,
            CreateBankAccount createBankAccount,
            FindBankAccount findBankAccount,
            CreatePersonCustomer createPersonCustomer,
            CreateCorporateCustomer createCorporateCustomer,
            FindCustomer findCustomer,
            DeleteCustomer deleteCustomer,
            UpdatePersonCustomer updatePersonCustomer,
            UpdateCorporateCustomer updateCorporateCustomer,
            CreateUser createUser,
            FindUser findUser,
            DeleteUser deleteUser,
            UpdateUser updateUser) {
        this.createTransfer = createTransfer;
        this.findTransfer = findTransfer;
        this.createBankAccount = createBankAccount;
        this.findBankAccount = findBankAccount;
        this.createPersonCustomer = createPersonCustomer;
        this.createCorporateCustomer = createCorporateCustomer;
        this.findCustomer = findCustomer;
        this.deleteCustomer = deleteCustomer;
        this.updatePersonCustomer = updatePersonCustomer;
        this.updateCorporateCustomer = updateCorporateCustomer;
        this.createUser = createUser;
        this.findUser = findUser;
        this.deleteUser = deleteUser;
        this.updateUser = updateUser;
    }

    public void createTransfer(Transfer transfer, User user) throws BussinesException {
        createTransfer.createTransfer(transfer, user);
    }

    public Transfer findTransferById(UUID id) throws NotFoundException {
        return findTransfer.findById(id);
    }

    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws NotFoundException {
        return findTransfer.findByAccount(accountNumber, user);
    }

    public void createBankAccount(BankAccount bankAccount, User user) throws BussinesException {
        createBankAccount.createBankAccount(bankAccount, user);
    }

    public BankAccount findBankAccountById(UUID id) throws NotFoundException {
        return findBankAccount.findById(id);
    }

    public BankAccount findBankAccountByNumber(int accountNumber, User user) throws NotFoundException {
        return findBankAccount.findByAccountNumber(accountNumber, user);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document, User user) throws NotFoundException {
        return findBankAccount.findByCustomerOwner(document, user);
    }

    public void createPersonCustomer(PersonCustomer personCustomer) throws BussinesException {
        createPersonCustomer.createPersonCustomer(personCustomer);
    }

    public void createCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException {
        createCorporateCustomer.createCorporateCustomer(corporateCustomer);
    }

    public Customer findCustomerByDocument(String document) throws NotFoundException {
        return findCustomer.findCustomer(document);
    }

    public void deleteCustomer(String document) throws BussinesException {
        deleteCustomer.deleteCustomer(document);
    }

    public void updatePersonCustomer(PersonCustomer personCustomer) throws BussinesException {
        updatePersonCustomer.updatePersonCustomer(personCustomer);
    }

    public void updateCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException {
        updateCorporateCustomer.updateCorporateCustomer(corporateCustomer);
    }

    public void createUser(User user) throws BussinesException {
        createUser.createUser(user);
    }

    public User findUserByDocument(String document) throws NotFoundException {
        return findUser.findByDocument(document);
    }

    public void deleteUser(String document) throws BussinesException {
        deleteUser.deleteUser(document);
    }

    public void updateUser(User user) throws BussinesException {
        updateUser.updateUser(user);
    }
}