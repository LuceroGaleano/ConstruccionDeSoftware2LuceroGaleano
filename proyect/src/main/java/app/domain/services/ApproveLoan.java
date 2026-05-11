package app.domain.services;

import java.math.BigDecimal;
import java.util.HashMap;
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
    private LoanPort loanPort;
    private BitacoraPort bitacoraPort;
    private BankAccountPort bankAccountPort;

    @Autowired
    public ApproveLoan(LoanPort loanPort, BitacoraPort bitacoraPort, BankAccountPort bankAccountPort) {
        this.loanPort = loanPort;
        this.bitacoraPort = bitacoraPort;
        this.bankAccountPort = bankAccountPort;
    }

    public void approveLoan(UUID idLoan, User user, BigDecimal approvedAmount) throws BussinesException {
        Loan loan = loanPort.findById(idLoan);

        //Validamos que existe el prestamo
        if (loan == null) {
            throw new BussinesException("Prestamo no encontrado");
        }

        //Validamos que el estado del prestamo este pendiente
        if (!loan.getLoanStatus().equals(LoanStatus.Requested)) {
            throw new BussinesException("Estado invalido");
        }

        BankAccount bankAccount = bankAccountPort.findByAccountNumber(loan.getDisburseAccount().getAccountNumber());
        if(bankAccount == null){
            throw new BussinesException("Cuenta de desembolso no encontrada");
        }

        //Validamos que el monto aprobado no este por debajo del minimo
        if(loan.getApprovedAmount().compareTo(getMinLoanAmount(bankAccount.getCurrencyType())) < 0){
            throw new BussinesException("No es posible crear el prestamo, el monto solicitado esta por debajo del minimo");
        }

        //El monto aprobado no puede ser mayor al solicitado
        if (approvedAmount.compareTo(loan.getRequestedAmount()) > 0) {
            throw new BussinesException("El monto aprobado no puede ser mayor al monto solicitado");
        }

        Map<String, Object> detailData = new HashMap<>();
        detailData.put("requestedAmount", loan.getRequestedAmount());
        detailData.put("approvedAmount", approvedAmount);
        detailData.put("interestRate", loan.getInterestRate());
        detailData.put("previousState", loan.getLoanStatus().toString());

        loan.setApprovedAmount(approvedAmount);
        loan.setLoanStatus(LoanStatus.Approved);
        loanPort.update(loan);

        detailData.put("newState", loan.getLoanStatus().toString());

        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.ApprovingLoan);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(idLoan);
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);
    }

    //Obtener minimo posible segun su moneda
    private BigDecimal getMinLoanAmount(CurrencyType currencyType){
        return switch (currencyType) {
            case COP -> BigDecimal.valueOf(100000);
            case USD -> BigDecimal.valueOf(50);
            case EUR -> BigDecimal.valueOf(50);
            case GBP -> BigDecimal.valueOf(50);
            default -> BigDecimal.ZERO;
        };
    }
}