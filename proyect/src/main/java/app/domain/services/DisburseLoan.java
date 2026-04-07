package app.domain.services;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Loan;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.LoanPort;

@Service
public class DisburseLoan {
    private LoanPort loanPort;
    private BankAccountPort bankAccountPort;

    @Autowired
    public DisburseLoan(LoanPort loanPort, BankAccountPort bankAccountPort){
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
    }

    public void disburseLoan(String loanId) throws BussinesException{
        Loan loan = loanPort.findById(loanId);

        if(loan == null){
            throw new BussinesException("Prestamo no encontrado");
        }

        if(!loan.getLoanStatus().equals(LoanStatus.Approved)){
            throw new BussinesException("El prestamo no esta aprobado");
        }

        BankAccount bankAccount = loan.getDisburseAccount();

        if(bankAccount == null){
            throw new BussinesException("La cuenta de desembolso no existe");
        }

        if(!bankAccount.getAccountStatus().equals(AccountStatus.Active)){
            throw new BussinesException("La cuenta no esta activa, no se permite el desembolso");
        }

        disburse(loan);
        loan.setLoanStatus(LoanStatus.Disburser);
        loan.setDisburseDate(new Date(System.currentTimeMillis()));
        loanPort.update(loan);

    }

    private void disburse(Loan loan){
        BankAccount disburseAccount = loan.getDisburseAccount();
        BigDecimal amount = loan.getApprovedAmount();

        disburseAccount.setCurrentBalance(disburseAccount.getCurrentBalance().add(amount));
    }
}
