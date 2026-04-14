package app.domain.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.TransferPort;
import app.domain.ports.UserPort;

@Service
public class ApproveTransfer {
    TransferPort transferPort;
    ExecuteTransfer executeTransfer;
    UserPort userPort;

    @Autowired
    public ApproveTransfer(TransferPort transferPort, ExecuteTransfer executeTransfer, UserPort userPort){
        this.transferPort = transferPort;
        this.executeTransfer = executeTransfer;
        this.userPort = userPort;
    }

    public void approveTransfer(String idTransfer) throws  BussinesException{
        Transfer transfer = transferPort.findById(idTransfer);
        
        if(transfer == null){
            throw new BussinesException("Transferencia no encontrada");
        }

        if(!transfer.getTransferStatus().equals(TransferStatus.Pending)){
            throw new BussinesException("Estado no válido");
        }

        //Validamos que el que aprueba la transferencia exista
        User approver = userPort.findByDocument(transfer.getIdApprover());

        if(approver == null){
            throw new BussinesException("Usuario que aprueba no encontrado");
        }

        //Validamos que el que aprueba la transferencia y el creador sean de la misma empresa
        User creator = userPort.findByDocument(transfer.getIdCreator());
        if(!approver.getCompany().equals(creator.getCompany())){
            throw new BussinesException("El usuario aprobador y el creador de la transferencia no pertenecen a la misma empresa");
         }

        transfer.setTransferStatus(TransferStatus.Approved);
        transfer.setApprovalDate(new Date(System.currentTimeMillis()));
        executeTransfer.executeTransfer(transfer);

        transferPort.update(transfer);
    }
}
