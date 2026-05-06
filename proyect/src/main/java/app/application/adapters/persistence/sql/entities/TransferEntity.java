package app.application.adapters.persistence.sql.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private BankAccountEntity originAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private BankAccountEntity destinationAccount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "approval_date")
    private Date approvalDate;

    @Column(name = "transfer_status")
    private String transferStatus;

    @Column(name = "id_creator")
    private String idCreator;

    @Column(name = "id_approver")
    private String idApprover;
}