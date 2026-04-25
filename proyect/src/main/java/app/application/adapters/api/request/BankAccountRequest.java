package app.application.adapters.api.request;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.Customer;
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
    private String id;
    private String ProductName;
    private ProductCategory productCategory;
    private Boolean approved;

    @Valid
    @NotNull(message = "El propietario del la cuenta bancaira es obligatorio")
    private Customer customerOwner;

    private int accountNumber;
    private BigDecimal currentBalance;
    private AccountStatus accountStatus;
    private Date openingDate;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType accountType;

    @NotNull(message = "El tipo de moneda es obligatorio")
    private CurrencyType currencyType;
}
