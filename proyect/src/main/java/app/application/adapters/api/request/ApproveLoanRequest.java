package app.application.adapters.api.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveLoanRequest {
    @NotNull(message = "El monto aprovado es obligatorio")
    private BigDecimal approvedAmount;
}
