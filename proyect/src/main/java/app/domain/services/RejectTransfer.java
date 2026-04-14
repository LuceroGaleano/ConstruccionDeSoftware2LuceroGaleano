package app.domain.services;

import app.domain.Exception.BussinesException;
import app.domain.models.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.TransferPort;

public class RejectTransfer {
    TransferPort transferPort;

    public void rejectTransfer(String idTransfer){
        Transfer transfer = transferPort.findById(idTransfer);
        
        if(transfer == null){
            throw new BussinesException("Transferencia no encontrada");
        }

        if(!transfer.getTransferStatus().equals(TransferStatus.Pending)){
            throw new BussinesException("Estado no válido");
        }

        transfer.setTransferStatus(TransferStatus.Rejected);
        transferPort.update(transfer);
    }
}
