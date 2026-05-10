package app.application.adapters.persistence.sql;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.BankAccountEntity;
import app.application.adapters.persistence.sql.entities.CorporateCustomerEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;
import app.application.adapters.persistence.sql.entities.PersonCustomerEntity;
import app.application.adapters.persistence.sql.repositories.BankAccountRepository;
import app.application.adapters.persistence.sql.repositories.CustomerRepository;
import app.domain.models.BankAccount;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.PersonCustomer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.CurrencyType;
import app.domain.models.enums.ProductCategory;
import app.domain.ports.BankAccountPort;

@Service
public class BankAccountPersistenceAdapater implements BankAccountPort{
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    public BankAccountPersistenceAdapater(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository){
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void save(BankAccount bankAccount){
        BankAccountEntity savedEntity = bankAccountRepository.save(toEntity(bankAccount));
        bankAccount.setId(savedEntity.getId());
    }

    @Override
    public void update(BankAccount bankAccount){
        BankAccountEntity existingAccount = bankAccountRepository.findById(bankAccount.getId()).orElse(null);
        if(existingAccount != null){
            existingAccount.setId(bankAccount.getId());
            existingAccount.setProductName(bankAccount.getProductName());
            existingAccount.setProductCategory(bankAccount.getProductCategory() != null ? bankAccount.getProductCategory().toString() : null);
            existingAccount.setApproved(bankAccount.isApproved());
            existingAccount.setAccountNumber(bankAccount.getAccountNumber());
            existingAccount.setAccountType(bankAccount.getAccountType() != null ? bankAccount.getAccountType().toString() : null);
            existingAccount.setCurrentBalance(bankAccount.getCurrentBalance());
            existingAccount.setCurrencyType(bankAccount.getCurrencyType() != null ? bankAccount.getCurrencyType().toString() : null);
            existingAccount.setAccountStatus(bankAccount.getAccountStatus() != null ? bankAccount.getAccountStatus().toString() : null);
            existingAccount.setOpeningDate(bankAccount.getOpeningDate());
            bankAccountRepository.save(existingAccount);
        }
    }

    @Override
    public boolean existsById(UUID id){
        return bankAccountRepository.existsById(id);
    }

    @Override
    public boolean existsByAccountNumber(int accountNumber){
        return bankAccountRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public BankAccount findById(UUID id){
        return toModel(bankAccountRepository.findById(id).orElse(null));
    }

    @Override
    public BankAccount findByAccountNumber(int accountNumber){
        return toModel(bankAccountRepository.findByAccountNumber(accountNumber));
    }


    @Override
    public List<BankAccount> findByCustomerOwner(Customer customer){
        CustomerEntity customerEntity = customerRepository.findByDocument(customer.getDocument());
        return bankAccountRepository.findByCustomerOwner(customerEntity).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private BankAccountEntity toEntity(BankAccount bankAccount){
        BankAccountEntity e = new BankAccountEntity();
        e.setProductName(bankAccount.getProductName());
        e.setProductCategory(bankAccount.getProductCategory() != null ? bankAccount.getProductCategory().toString() : null);
        e.setApproved(bankAccount.isApproved());
        e.setAccountNumber(bankAccount.getAccountNumber());
        e.setAccountType(bankAccount.getAccountType() != null ? bankAccount.getAccountType().toString() : null);
        e.setCurrentBalance(bankAccount.getCurrentBalance());
        e.setCurrencyType(bankAccount.getCurrencyType() != null ? bankAccount.getCurrencyType().toString() : null);
        e.setAccountStatus(bankAccount.getAccountStatus() != null ? bankAccount.getAccountStatus().toString() : null);
        e.setOpeningDate(bankAccount.getOpeningDate());
        if(bankAccount.getCustomerOwner() != null){
            CustomerEntity customerEntity = customerRepository.findByDocument(bankAccount.getCustomerOwner().getDocument());
            e.setCustomerOwner(customerEntity);
        }
        return e;
    }

    private BankAccount toModel(BankAccountEntity e){
        if(e == null) return null;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(e.getId());
        bankAccount.setProductName(e.getProductName());
        bankAccount.setProductCategory(e.getProductCategory() != null ? ProductCategory.valueOf(e.getProductCategory()) : null);
        bankAccount.setApproved(e.isApproved());
        bankAccount.setAccountNumber(e.getAccountNumber());
        bankAccount.setAccountType(e.getAccountType() != null ? AccountType.valueOf(e.getAccountType()) : null);
        bankAccount.setCurrentBalance(e.getCurrentBalance());
        bankAccount.setCurrencyType(e.getCurrencyType() != null ? CurrencyType.valueOf(e.getCurrencyType()) : null);
        bankAccount.setAccountStatus(e.getAccountStatus() != null ? AccountStatus.valueOf(e.getAccountStatus()) : null);
        bankAccount.setOpeningDate(e.getOpeningDate());
        if (e.getCustomerOwner() instanceof PersonCustomerEntity) {
            PersonCustomer customer = new PersonCustomer();
            customer.setDocument(e.getCustomerOwner().getDocument());
            customer.setFullName(e.getCustomerOwner().getFullName());
            bankAccount.setCustomerOwner(customer);
        } else if (e.getCustomerOwner() instanceof CorporateCustomerEntity) {
            CorporateCustomer customer = new CorporateCustomer();
            customer.setDocument(e.getCustomerOwner().getDocument());
            customer.setFullName(e.getCustomerOwner().getFullName());
            bankAccount.setCustomerOwner(customer);
        }
        return bankAccount;
    }
}