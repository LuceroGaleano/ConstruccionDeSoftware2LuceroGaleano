package app.application.adapters.api.request;

import java.math.BigDecimal;
import java.util.Date;

import app.domain.models.enums.TransferStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {
    @NotNull(message = "La cuenta de origen es obligatoria")
    private int originAccountNumber;

    @NotNull(message = "La cuenta de destino es obligatoria")
    private int destinationAccountNumber;

    @NotNull(message = "El monto de la transferencia es obligatorio")
    private BigDecimal amount;

    private Date creationDate;

    private Date approvalDate;
    
    private TransferStatus transferStatus;

    @NotBlank(message = "El documento de quien esta creando la transferencia no puede estar vacío")
    private String idCreator;
    
    private String idApprover;
}
