package app.application.adapters.persistence.sql;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.CorporateCustomerEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;
import app.application.adapters.persistence.sql.entities.LoanEntity;
import app.application.adapters.persistence.sql.entities.PersonCustomerEntity;
import app.application.adapters.persistence.sql.repositories.BankAccountRepository;
import app.application.adapters.persistence.sql.repositories.CustomerRepository;
import app.application.adapters.persistence.sql.repositories.LoanRepository;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.LoanType;
import app.domain.models.enums.ProductCategory;
import app.domain.ports.LoanPort;

@Service
public class LoanPersistenceAdapter implements LoanPort {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;

    public LoanPersistenceAdapter(LoanRepository loanRepository, CustomerRepository customerRepository, BankAccountRepository bankAccountRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return loanRepository.existsById(id);
    }

    @Override
    public void save(Loan loan) {
        LoanEntity savedEntity = loanRepository.save(toEntity(loan));
        loan.setId(savedEntity.getId());
    }

    @Override
    public void update(Loan loan) {
        LoanEntity existingLoan = loanRepository.findById(loan.getId()).orElse(null);
        if (existingLoan != null) {
            existingLoan.setId(loan.getId());
            existingLoan.setProductName(loan.getProductName());
            existingLoan.setProductCategory(loan.getProductCategory() != null ? loan.getProductCategory().toString() : null);
            existingLoan.setLoanType(loan.getLoanType() != null ? loan.getLoanType().toString() : null);
            existingLoan.setRequestedAmount(loan.getRequestedAmount());
            existingLoan.setApprovedAmount(loan.getApprovedAmount());
            existingLoan.setInterestRate(loan.getInterestRate());
            existingLoan.setTermInMonths(loan.getTermInMonths());
            existingLoan.setLoanStatus(loan.getLoanStatus() != null ? loan.getLoanStatus().toString() : null);
            existingLoan.setCreateDate(loan.getCreateDate());
            existingLoan.setApprovalDate(loan.getApprovalDate());
            existingLoan.setDisburseDate(loan.getDisburseDate());
            loanRepository.save(existingLoan);
        }
    }

    @Override
    public Loan findById(UUID id) {
        return loanRepository.findById(id).map(this::toModel).orElse(null);
    }

    @Override
    public List<Loan> findByCustomer(Customer customer) {
        CustomerEntity customerEntity = customerRepository.findByDocument(customer.getDocument());
        return loanRepository.findByCustomerOwner(customerEntity).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

private LoanEntity toEntity(Loan loan) {
    LoanEntity e = new LoanEntity();
    e.setProductName(loan.getProductName());
    e.setProductCategory(loan.getProductCategory() != null ? loan.getProductCategory().toString() : null);
    e.setApproved(loan.isApproved());
    e.setLoanType(loan.getLoanType() != null ? loan.getLoanType().toString() : null);
    e.setRequestedAmount(loan.getRequestedAmount());
    e.setApprovedAmount(loan.getApprovedAmount());
    e.setInterestRate(loan.getInterestRate());
    e.setTermInMonths(loan.getTermInMonths());
    e.setLoanStatus(loan.getLoanStatus() != null ? loan.getLoanStatus().toString() : null);
    e.setCreateDate(loan.getCreateDate());
    e.setApprovalDate(loan.getApprovalDate());
    e.setDisburseDate(loan.getDisburseDate());
    e.setCustomerOwner(customerRepository.findByDocument(loan.getCustomerOwner().getDocument()));
    if(loan.getDisburseAccount() != null){
        e.setDisburseAccount(bankAccountRepository.findByAccountNumber(loan.getDisburseAccount().getAccountNumber()));
    }
    return e;
}

    private Loan toModel(LoanEntity e) {
        if (e == null) return null;
        Loan loan = new Loan();
        loan.setId(e.getId());
        loan.setProductName(e.getProductName());
        loan.setProductCategory(e.getProductCategory() != null ? ProductCategory.valueOf(e.getProductCategory()) : null);
        loan.setApproved(e.isApproved());
        loan.setLoanType(e.getLoanType() != null ? LoanType.valueOf(e.getLoanType()) : null);
        loan.setRequestedAmount(e.getRequestedAmount());
        loan.setApprovedAmount(e.getApprovedAmount());
        loan.setInterestRate(e.getInterestRate());
        loan.setTermInMonths(e.getTermInMonths());
        loan.setLoanStatus(e.getLoanStatus() != null ? LoanStatus.valueOf(e.getLoanStatus()) : null);
        loan.setCreateDate(e.getCreateDate());
        loan.setApprovalDate(e.getApprovalDate());
        loan.setDisburseDate(e.getDisburseDate());
        if (e.getCustomerOwner() instanceof PersonCustomerEntity) {
            PersonCustomer customer = new PersonCustomer();
            customer.setDocument(e.getCustomerOwner().getDocument());
            customer.setFullName(e.getCustomerOwner().getFullName());
            loan.setCustomerOwner(customer);
        } else if (e.getCustomerOwner() instanceof CorporateCustomerEntity) {
            CorporateCustomer customer = new CorporateCustomer();
            customer.setDocument(e.getCustomerOwner().getDocument());
            customer.setFullName(e.getCustomerOwner().getFullName());
            loan.setCustomerOwner(customer);
        }
        // Mapear disburseAccount
        if (e.getDisburseAccount() != null) {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setId(e.getDisburseAccount().getId());
            bankAccount.setAccountNumber(e.getDisburseAccount().getAccountNumber());
            bankAccount.setCurrentBalance(e.getDisburseAccount().getCurrentBalance());
            bankAccount.setAccountStatus(e.getDisburseAccount().getAccountStatus() != null ?
                    AccountStatus.valueOf(e.getDisburseAccount().getAccountStatus()) : null);
            loan.setDisburseAccount(bankAccount);
        }
        return loan;
    }
}