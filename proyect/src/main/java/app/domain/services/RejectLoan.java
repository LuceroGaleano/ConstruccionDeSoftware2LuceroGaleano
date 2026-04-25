package app.domain.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.ports.LoanPort;

@Service
public class RejectLoan {
        private final LoanPort loanPort;

    @Autowired
    public RejectLoan(LoanPort loanPort){
        this.loanPort = loanPort;
    }

    public void approveLoan(String idLoan) throws BussinesException{
        Loan loan = loanPort.findById(idLoan);

        if(loan == null){
            throw new BussinesException("Prestamo no encontrado");
        }

        if(!loan.getLoanStatus().equals(LoanStatus.Requested)){
            throw new BussinesException("Estado inválido");
        }

        loan.setLoanStatus(LoanStatus.Rejected);
        loan.setDisburseDate(new Date(System.currentTimeMillis()));

        loanPort.update(loan);
    }
}
