package application.adapters.api.response;

import java.math.BigDecimal;
import java.sql.Date;

import app.domain.models.Customer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.CurrencyType;
import app.domain.models.enums.ProductCategory;

public record BankAccountResponse(
    String productID,
    String ProductName,
    ProductCategory productCategory,
    Boolean approved,
    Customer customerOwner,
    int accountNumber,
    BigDecimal currentBalance,
    AccountStatus accountStatus,
    Date openingDate,
    AccountType accountType,
    CurrencyType currencyType
) {}
