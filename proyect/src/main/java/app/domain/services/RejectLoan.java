package app.domain.services;

import java.sql.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Bitacora;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.OperationBitacora;
import app.domain.ports.BitacoraPort;
import app.domain.ports.LoanPort;

@Service
public class RejectLoan {
    private final LoanPort loanPort;
    private final BitacoraPort bitacoraPort;

    @Autowired
    public RejectLoan(LoanPort loanPort, BitacoraPort bitacoraPort){
        this.loanPort = loanPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void rejectLoan(UUID idLoan, User user) throws BussinesException{
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

        //Bitacora
        Map<String, Object> detailData = Map.of(
            "requestedAmount: ", loan.getRequestedAmount(),
            "InterestRate", loan.getInterestRate(),
            "previousState", loan.getLoanStatus()
        );


        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.RejectionLoan);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(loan.getId());
        bitacora.setDetailData(detailData);
        bitacoraPort.save(bitacora);
    }
}
