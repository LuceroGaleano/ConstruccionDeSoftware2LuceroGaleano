package app.domain.models;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class BankAccount extends Product{
    private int accountNumber;
    private AccountType accountType;
    private Customer accountHolder;
    private double currentBalance;
    private Currency currencyType;
    private AccountStatus accountStatus;
    private Date openingDate;
}

