package application.adapters.api.response;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.BankAccount;
import app.domain.models.Customer;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.LoanType;
import app.domain.models.enums.ProductCategory;

public record LoanResponse(
    String productID,
    String ProductName,
    ProductCategory productCategory,
    Boolean approved,
    Customer customerOwner,
    LoanType loanType,
    Customer customerApplicant,
    BigDecimal requestedAmount,
    BigDecimal approvedAmount,
    double interestRate,
    int termInMonths,
    LoanStatus loanStatus,
    Date createDate,
    Date approvalDate,
    Date disburseDate,
    BankAccount disburseAccount
) {}
