package app.domain.models;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Transfer extends Product{
    private BankAccount OriginAccount;
    private BankAccount DestinationAccount;
    private double amount;
    private Date creationDate;
    private Date approvalDate;
    private TransferStatus transferStatus;
}
