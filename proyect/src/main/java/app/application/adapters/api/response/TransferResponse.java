package app.application.adapters.api.response;

import java.math.BigDecimal;
import java.util.Date;

import app.domain.models.BankAccount;
import app.domain.models.enums.TransferStatus;

public record TransferResponse(
    String id,
    BankAccount OriginAccount,
    BankAccount DestinationAccount,
    BigDecimal amount,
    Date creationDate,
    Date approvalDate,
    TransferStatus transferStatus,
    String idCreator,
    String idApprover
) {}
