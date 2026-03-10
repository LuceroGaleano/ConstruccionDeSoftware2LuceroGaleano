package app.domain.models;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Loan extends Product{
    private LoanType loanType;
    private Customer customerApplicant;
    private double requestedAmount;
    private double approvedAmount;
    private double interestRate;
    private int termInMonths;
    private String loanStatus;
    private Date aprovalDate;
    private Date disburseDate;
    private BankAccount disburseAccount;
}
