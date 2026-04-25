package app.domain.models;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.LoanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Loan extends Product{
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private double interestRate;
    private int termInMonths;
    private LoanStatus loanStatus;
    private Date createDate;
    private Date approvalDate;
    private Date disburseDate;
    private BankAccount disburseAccount;
}
