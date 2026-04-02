package app.domain.models;

import java.math.BigDecimal;
import java.util.Date;

import app.domain.models.enums.TransferStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Transfer{
    private String idTransfer;
    private BankAccount OriginAccount;
    private BankAccount DestinationAccount;
    private BigDecimal amount;
    private Date creationDate;
    private Date approvalDate;
    private TransferStatus transferStatus;
}
