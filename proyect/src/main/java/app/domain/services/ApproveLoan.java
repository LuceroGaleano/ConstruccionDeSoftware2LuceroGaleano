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
public class ApproveLoan {
    private LoanPort loanPort;
    private BitacoraPort bitacoraPort;

    @Autowired
    public ApproveLoan(LoanPort loanPort, BitacoraPort bitacoraPort){
        this.loanPort = loanPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void approveLoan(UUID idLoan, User user) throws BussinesException{
        Loan loan = loanPort.findById(idLoan);

        if(loan == null){
            throw new BussinesException("Prestamo no encontrado");
        }

        if(!loan.getLoanStatus().equals(LoanStatus.Requested)){
            throw new BussinesException("Estado invalido");
        }

        //Detalles para bitacora
        Map<String, Object> detailData = Map.of(
            "approvedAmmount: ", loan.getApprovedAmount(),
            "InterestRate", loan.getInterestRate(),
            "previousState", loan.getLoanStatus()
        );

        loan.setLoanStatus(LoanStatus.Approved);
        loan.setDisburseDate(new Date(System.currentTimeMillis()));

        loanPort.update(loan);
        detailData = Map.of(
            "newState: ", loan.getLoanStatus()
        );

        //Bitacora
        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.ApprovingLoan);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());        
        bitacora.setProductId(idLoan);
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);
    }
}