package app.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Bitacora;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.ApproveLoan;
import app.domain.services.FindBankAccount;
import app.domain.services.FindBitacora;
import app.domain.services.FindCustomer;
import app.domain.services.FindLoan;
import app.domain.services.FindTransfer;
import app.domain.services.RejectLoan;

@Service
public class InternalAnalystUseCase {

    private final FindCustomer findCustomer;
    private final FindLoan findLoan;
    private final FindTransfer findTransfer;
    private final FindBankAccount findBankAccount;
    private final FindBitacora findBitacora;
    private final ApproveLoan approveLoan;
    private final RejectLoan rejectLoan;

    public InternalAnalystUseCase(
            FindCustomer findCustomer,
            FindLoan findLoan,
            FindTransfer findTransfer,
            FindBankAccount findBankAccount,
            FindBitacora findBitacora,
            ApproveLoan approveLoan,
            RejectLoan rejectLoan) {
        this.findCustomer = findCustomer;
        this.findLoan = findLoan;
        this.findTransfer = findTransfer;
        this.findBankAccount = findBankAccount;
        this.findBitacora = findBitacora;
        this.approveLoan = approveLoan;
        this.rejectLoan = rejectLoan;
    }

    public Customer findCustomerByDocument(String document) throws BussinesException {
        return findCustomer.findCustomer(document);
    }

    public Loan findLoanById(UUID id) throws BussinesException {
        return findLoan.findById(id);
    }

    public List<Loan> findLoansByCustomer(String document, User user) throws BussinesException {
        return findLoan.findByCustomer(document, user);
    }

    public Transfer findTransferById(UUID id) throws BussinesException {
        return findTransfer.findById(id);
    }

    public List<Transfer> findTransfersByAccount(int accountNumber, User user) throws BussinesException {
        return findTransfer.findByAccount(accountNumber, user);
    }

    public BankAccount findBankAccountById(UUID id) throws BussinesException {
        return findBankAccount.findById(id);
    }

    public List<BankAccount> findBankAccountsByCustomer(String document, User user) throws BussinesException {
        return findBankAccount.findByCustomerOwner(document, user);
    }

    public Bitacora findBitacoraById(String id) throws BussinesException {
        return findBitacora.findById(id);
    }

    public void approveLoan(UUID id, User user) throws BussinesException {
        approveLoan.approveLoan(id, user);
    }

    public void rejectLoan(UUID id, User user) throws BussinesException {
        rejectLoan.rejectLoan(id, user);
    }
}