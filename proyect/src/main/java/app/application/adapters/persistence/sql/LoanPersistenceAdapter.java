package app.application.adapters.persistence.sql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.CorporateCustomerEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;
import app.application.adapters.persistence.sql.entities.LoanEntity;
import app.application.adapters.persistence.sql.entities.PersonCustomerEntity;
import app.application.adapters.persistence.sql.repositories.CustomerRepository;
import app.application.adapters.persistence.sql.repositories.LoanRepository;
import app.domain.models.CorporateCustomer;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.PersonCustomer;
import app.domain.ports.LoanPort;

@Service
public class LoanPersistenceAdapter implements LoanPort {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    public LoanPersistenceAdapter(LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean existsById(String id) {
        return loanRepository.existsById(id);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(toEntity(loan));
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
            existingLoan.setInterestRate((double) loan.getInterestRate());
            existingLoan.setTermInMonths(loan.getTermInMonths());
            existingLoan.setCreateDate(loan.getCreateDate());   
            existingLoan.setApprovalDate(loan.getApprovalDate());
            existingLoan.setDisburseDate(loan.getDisburseDate());

            loanRepository.save(existingLoan);
        }
    }

    @Override
    public Loan findById(String id) {
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
        e.setCreateDate(loan.getCreateDate());
        e.setCustomerOwner(customerRepository.findByDocument(loan.getCustomerOwner().getDocument()));
        return e;
    }

    private Loan toModel(LoanEntity e) {
        if (e == null) return null;
        Loan loan = new Loan();
        loan.setId(e.getId());
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
        return loan;
    }
}