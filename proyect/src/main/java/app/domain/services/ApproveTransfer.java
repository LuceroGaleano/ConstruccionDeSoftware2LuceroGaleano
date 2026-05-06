package app.domain.services;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Bitacora;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.models.enums.OperationBitacora;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BitacoraPort;
import app.domain.ports.TransferPort;
import app.domain.ports.UserPort;

@Service
public class ApproveTransfer {
    private final TransferPort transferPort;
    private final ExecuteTransfer executeTransfer;
    private final UserPort userPort;
    private final BitacoraPort bitacoraPort;

    @Autowired
    public ApproveTransfer(TransferPort transferPort, ExecuteTransfer executeTransfer, UserPort userPort, BitacoraPort bitacoraPort){
        this.transferPort = transferPort;
        this.executeTransfer = executeTransfer;
        this.userPort = userPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void approveTransfer(UUID id, User user) throws  BussinesException{
        Transfer transfer = transferPort.findById(id);
        
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

        Map<String, Object> detailData = Map.of(
            "balanceBeforeOrigin", transfer.getOriginAccount().getCurrentBalance(),
            "balanceBeforeDestination", transfer.getDestinationAccount().getCurrentBalance()
        );

        transfer.setTransferStatus(TransferStatus.Approved);
        transfer.setApprovalDate(new Date(System.currentTimeMillis()));
        executeTransfer.executeTransfer(transfer);

        transferPort.update(transfer);

        //Bitacora
        detailData = Map.of(
            "amountTransfer", transfer.getAmount(),
            "balanceAfterOrigin", transfer.getOriginAccount().getCurrentBalance(),
            "balanceAfterDestination", transfer.getDestinationAccount().getCurrentBalance()
        );

        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.ApprovingTransfer);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(transfer.getId());
        bitacora.setDetailData(detailData);

        bitacoraPort.save(bitacora);
    }
}
