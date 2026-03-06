package app.domain.models;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Transfer{
    private long idTransfer;
    private BankAccount OriginAccount;
    private BankAccount DestinationAccount;
    private double amount;
    private Date creationDate;
    private Date approvalDate;
    private TransferStatus transferStatus;
}
