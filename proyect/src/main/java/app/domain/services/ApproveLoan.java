package app.domain.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Bitacora;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.models.enums.CurrencyType;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.OperationBitacora;
import app.domain.ports.BankAccountPort;
import app.domain.ports.BitacoraPort;
import app.domain.ports.LoanPort;

@Service
public class ApproveLoan {

    private final LoanPort loanPort;
    private final BitacoraPort bitacoraPort;
    private final BankAccountPort bankAccountPort;

    @Autowired
    public ApproveLoan(
            LoanPort loanPort,
            BitacoraPort bitacoraPort,
            BankAccountPort bankAccountPort) {

        this.loanPort = loanPort;
        this.bitacoraPort = bitacoraPort;
        this.bankAccountPort = bankAccountPort;
    }

    public void approveLoan(UUID idLoan, User user, BigDecimal approvedAmount)
            throws BussinesException {

        Loan loan = loanPort.findById(idLoan);

        if (loan == null) {
            throw new BussinesException("Prestamo no encontrado");
        }

        if (!loan.getLoanStatus().equals(LoanStatus.Requested)) {
            throw new BussinesException("Estado invalido");
        }

        BankAccount bankAccount =
                bankAccountPort.findByAccountNumber(
                        loan.getDisburseAccount().getAccountNumber());

        if (bankAccount == null) {
            throw new BussinesException("Cuenta de desembolso no encontrada");
        }

        if (approvedAmount.compareTo(
                getMinLoanAmount(bankAccount.getCurrencyType())) < 0) {

            throw new BussinesException(
                    "No es posible crear el prestamo, el monto solicitado esta por debajo del minimo");
        }

        if (approvedAmount.compareTo(loan.getRequestedAmount()) > 0) {
            throw new BussinesException(
                    "El monto aprobado no puede ser mayor al monto solicitado");
        }

        LoanStatus previousState = loan.getLoanStatus();

        loan.setApprovedAmount(approvedAmount);
        
        //Validamos el monto aprobado
        if(loan.getApprovedAmount() == null){
            throw new BussinesException("Inserta el monto aprobado");
        }

        loan.setLoanStatus(LoanStatus.Approved);

        loanPort.update(loan);

        Map<String, Object> detailData = Map.of(
                "requestedAmount", loan.getRequestedAmount(),
                "approvedAmount", approvedAmount,
                "interestRate", loan.getInterestRate(),
                "previousState", previousState.toString(),
                "newState", loan.getLoanStatus().toString()
        );

        Bitacora bitacora = new Bitacora();

        bitacora.setOperationType(OperationBitacora.ApprovingLoan);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(idLoan);
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);
    }

    private BigDecimal getMinLoanAmount(CurrencyType currencyType) {
        return switch (currencyType) {
            case USD -> BigDecimal.valueOf(50);
            case EUR -> BigDecimal.valueOf(50);
            case GBP -> BigDecimal.valueOf(50);
            default -> BigDecimal.ZERO;
        };
    }
}