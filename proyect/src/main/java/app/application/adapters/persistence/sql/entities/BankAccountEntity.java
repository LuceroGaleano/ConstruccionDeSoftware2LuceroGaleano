package app.application.adapters.persistence.sql.entities;

import java.math.BigDecimal;
import java.sql.Date;

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
@Table(name = "bank_accounts")
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "approved")
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customerOwner;

    @Column(name = "account_number")
    private int accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "currency_type")
    private String currencyType;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "opening_date")
    private Date openingDate;
}