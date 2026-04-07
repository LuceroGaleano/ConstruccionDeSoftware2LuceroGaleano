package app.domain.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.TransferPort;

@Service
public class ApproveTransfer {
    TransferPort transferPort;
    ExecuteTransfer executeTransfer;

    @Autowired
    public ApproveTransfer(TransferPort transferPort, ExecuteTransfer executeTransfer){
        this.transferPort = transferPort;
        this.executeTransfer = executeTransfer;
    }

    public void approveTranfer(String idTransfer) throws  BussinesException{
        Transfer transfer = transferPort.findById(idTransfer);
        
        if(transfer == null){
            throw new BussinesException("Transferencia no encontrada");
        }

        if(!transfer.getTransferStatus().equals(TransferStatus.Pending)){
            throw new BussinesException("Estado no valido");
        }

        transfer.setTransferStatus(TransferStatus.Approved);
        transfer.setApprovalDate(new Date(System.currentTimeMillis()));
        executeTransfer.executeTransfer(transfer);

        transferPort.update(transfer);
    }
}
