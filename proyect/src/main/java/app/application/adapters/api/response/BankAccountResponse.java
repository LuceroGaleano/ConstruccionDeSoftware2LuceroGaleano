package app.application.adapters.api.response;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.CurrencyType;
import app.domain.models.enums.ProductCategory;

public record BankAccountResponse(
    UUID id,
    String ProductName,
    ProductCategory productCategory,
    Boolean approved,
    CustomerResponse customerOwner,
    int accountNumber,
    BigDecimal currentBalance,
    AccountStatus accountStatus,
    Date openingDate,
    AccountType accountType,
    CurrencyType currencyType
) {}
