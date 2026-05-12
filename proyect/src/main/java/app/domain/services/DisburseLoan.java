package app.domain.services;

import java.math.BigDecimal;
import java.sql.Date;
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
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.OperationBitacora;
import app.domain.ports.BankAccountPort;
import app.domain.ports.BitacoraPort;
import app.domain.ports.LoanPort;

@Service
public class DisburseLoan {
    private LoanPort loanPort;
    private BankAccountPort bankAccountPort;
    private BitacoraPort bitacoraPort;

    @Autowired
    public DisburseLoan(LoanPort loanPort, BankAccountPort bankAccountPort, BitacoraPort bitacoraPort) {
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void disburseLoan(UUID loanId, User user) throws BussinesException {
        // Validar que el usuario exista
        if (user == null) {
            throw new BussinesException("Usuario no encontrado");
        }

        // Validar que el id del préstamo exista
        if (loanId == null) {
            throw new BussinesException("Id del préstamo inválido");
        }
        Loan loan = loanPort.findById(loanId);

        if (loan == null) {
            throw new BussinesException("Prestamo no encontrado");
        }

        if (!loan.getLoanStatus().equals(LoanStatus.Approved)) {
            throw new BussinesException("El prestamo no esta aprobado");
        }

        BankAccount bankAccount = bankAccountPort.findById(
                loan.getDisburseAccount().getId()
        );

        if (bankAccount == null) {
            throw new BussinesException("La cuenta de desembolso no existe");
        }

        if (!bankAccount.getAccountStatus().equals(AccountStatus.Active)) {
            throw new BussinesException("La cuenta no esta activa, no se permite el desembolso");
        }

        Map<String, Object> detailData = new HashMap<>();
        detailData.put("approvedAmount", loan.getApprovedAmount());
        detailData.put("accountNumber", bankAccount.getAccountNumber());
        detailData.put("balanceBefore", bankAccount.getCurrentBalance());

        disburse(loan);

        loan.setLoanStatus(LoanStatus.Disburser);
        loan.setDisburseDate(new Date(System.currentTimeMillis()));
        loanPort.update(loan);

        detailData.put("balanceAfter", bankAccount.getCurrentBalance());

        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.DisbursementLoan);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(loanId);
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);
    }

    private void disburse(Loan loan) {
        BankAccount disburseAccount = bankAccountPort.findById(
                loan.getDisburseAccount().getId()
        );
        BigDecimal amount = loan.getApprovedAmount();
        disburseAccount.setCurrentBalance(disburseAccount.getCurrentBalance().add(amount));
        bankAccountPort.update(disburseAccount);
    }
}