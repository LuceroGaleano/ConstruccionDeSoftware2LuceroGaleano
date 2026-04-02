package app.domain.models;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class BankAccount extends Product{
    private int accountNumber;
    private AccountType accountType;
    private BigDecimal currentBalance;
    private Currency currencyType;
    private AccountStatus accountStatus;
    private Date openingDate;
}

