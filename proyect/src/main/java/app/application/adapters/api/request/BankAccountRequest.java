package app.application.adapters.api.request;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.CurrencyType;
import app.domain.models.enums.ProductCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountRequest {
    private String ProductName;
    private ProductCategory productCategory;
    private Boolean approved;
    
    @Valid
    @NotNull(message = "El propietario de la cuenta bancaria es obligatorio")
    private CustomerRequest customerOwner;

    private int accountNumber;
    private BigDecimal currentBalance;
    private AccountStatus accountStatus;
    private Date openingDate;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType accountType;

    @NotNull(message = "El tipo de moneda es obligatorio")
    private CurrencyType currencyType;
}
