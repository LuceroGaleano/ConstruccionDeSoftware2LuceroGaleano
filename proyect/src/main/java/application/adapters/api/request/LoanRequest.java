package application.adapters.api.request;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.BankAccount;
import app.domain.models.Customer;
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
    private String productID;
    private String ProductName;
    private ProductCategory productCategory;
    private Boolean approved;

    @Valid
    @NotNull(message = "El propietario del préstamo es obligatorio")
    private Customer customerOwner;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    private LoanType loanType;

    @Valid
    @NotNull(message = "El solicitante del préstamo es obligatorio")
    private Customer customerApplicant;

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
