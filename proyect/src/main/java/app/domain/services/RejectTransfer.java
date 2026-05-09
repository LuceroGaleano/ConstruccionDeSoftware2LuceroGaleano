package app.domain.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Bitacora;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.OperationBitacora;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BitacoraPort;
import app.domain.ports.TransferPort;

@Service
public class RejectTransfer {
    private final TransferPort transferPort;
    private final BitacoraPort bitacoraPort;

    public RejectTransfer(TransferPort transferPort, BitacoraPort bitacoraPort) {
        this.transferPort = transferPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void rejectTransfer(UUID idTransfer, User user){
        Transfer transfer = transferPort.findById(idTransfer);
        
        if(transfer == null){
            throw new BussinesException("Transferencia no encontrada");
        }

        if(!transfer.getTransferStatus().equals(TransferStatus.Pending)){
            throw new BussinesException("Estado no válido");
        }

        transfer.setTransferStatus(TransferStatus.Rejected);
        transferPort.update(transfer);

        //Bitacora
        Map<String, Object> detailData = Map.of(
            "amountTransfer", transfer.getAmount(),
            "balanceOrigin", transfer.getOriginAccount().getCurrentBalance(),
            "balanceDestination", transfer.getDestinationAccount().getCurrentBalance()
        );

        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.RejectionTransfer);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(transfer.getId());
        bitacora.setDetailData(detailData);
        bitacoraPort.save(bitacora);
    }
}
