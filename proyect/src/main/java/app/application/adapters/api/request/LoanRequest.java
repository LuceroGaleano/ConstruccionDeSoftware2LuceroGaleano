package app.application.adapters.api.request;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.BankAccount;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.LoanType;
import app.domain.models.enums.ProductCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequest {
    private String id;
    private String ProductName;
    private ProductCategory productCategory;
    private boolean approved;
    

    @Valid
    @NotNull(message = "El propietario del préstamo es obligatorio")
    private CustomerRequest customerOwner;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    private LoanType loanType;

    @NotNull(message = "El monto solicitado es obligatorio")
    private BigDecimal requestedAmount;

    private BigDecimal approvedAmount;
    private double interestRate;
    private int termInMonths;
    private LoanStatus loanStatus;
    private Date createDate;
    private Date approvalDate;
    private Date disburseDate;

    @Valid
    private BankAccount disburseAccount;
}
