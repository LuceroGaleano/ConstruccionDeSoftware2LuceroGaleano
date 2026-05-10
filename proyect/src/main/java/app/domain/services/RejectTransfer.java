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
import app.domain.ports.UserPort;

@Service
public class RejectTransfer {
    private final TransferPort transferPort;
    private final BitacoraPort bitacoraPort;
    private final UserPort userPort;

    public RejectTransfer(TransferPort transferPort, BitacoraPort bitacoraPort, UserPort userPort) {
        this.transferPort = transferPort;
        this.bitacoraPort = bitacoraPort;
        this.userPort = userPort;
    }

    public void rejectTransfer(UUID idTransfer, User user) throws BussinesException {
        Transfer transfer = transferPort.findById(idTransfer);

        if (transfer == null) {
            throw new BussinesException("Transferencia no encontrada");
        }

        if (!transfer.getTransferStatus().equals(TransferStatus.Pending)) {
            throw new BussinesException("Estado no válido");
        }

        // El que rechaza es el usuario autenticado
        User rejecter = user;

        // Validamos que el creador exista
        User creator = userPort.findByDocument(transfer.getIdCreator());
        if (creator == null) {
            throw new BussinesException("Usuario creador no encontrado");
        }

        // Validamos que el que rechaza y el creador sean de la misma empresa
        if (!rejecter.getCustomer().getDocument().equals(creator.getCustomer().getDocument())) {
            throw new BussinesException("El usuario que rechaza y el creador no pertenecen a la misma empresa");
        }

        transfer.setTransferStatus(TransferStatus.Rejected);
        transfer.setIdApprover(rejecter.getDocument());
        transferPort.update(transfer);

        // Bitacora
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