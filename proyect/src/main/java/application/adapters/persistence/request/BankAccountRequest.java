package application.adapters.persistence.request;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Currency;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountRequest extends ProductRequest {
    private int accountNumber;
    private BigDecimal currentBalance;
    private AccountStatus accountStatus;
    private Date openingDate;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType accountType;

    @NotNull(message = "El tipo de moneda es obligatorio")
    private Currency currencyType;
}
