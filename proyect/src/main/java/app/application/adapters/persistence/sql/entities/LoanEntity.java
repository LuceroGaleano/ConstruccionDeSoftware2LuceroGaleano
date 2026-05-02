package app.application.adapters.persistence.sql.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "loans")

public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "approved")
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "customer")
    private CustomerEntity customerOwner;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "requested_amount")
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;  

    @Column(name = "interest_rate")
    private double interestRate;        

    @Column(name = "term_in_months")
    private int termInMonths;

    @Column(name = "loan_status")
    private String loanStatus;

    @Column(name = "create_date")
    private Date createDate;
    
    @Column(name = "approval_date")
    private Date approvalDate;

    @Column(name = "disburse_date")
    private Date disburseDate;

    @ManyToOne
    @JoinColumn(name = "disburse_account_id") 
    private BankAccountEntity disburseAccount;
}

