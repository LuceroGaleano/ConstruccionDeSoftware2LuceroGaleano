package app.application.adapters.api.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import app.domain.models.BankAccount;
import app.domain.models.enums.TransferStatus;

public record TransferResponse(
    UUID id,
    BankAccount OriginAccount,
    BankAccount DestinationAccount,
    BigDecimal amount,
    Date creationDate,
    Date approvalDate,
    TransferStatus transferStatus,
    String idCreator,
    String idApprover
) {}
