package application.adapters.persistence.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorporateCustomerRequest extends CustomerRequest{
    @Valid
    @NotNull(message = "El representante legal es obligatorio")
    private PersonCustomerRequest legalRepresentative;
}
