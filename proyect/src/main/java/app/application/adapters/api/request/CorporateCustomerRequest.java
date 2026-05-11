package app.application.adapters.api.request;

import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorporateCustomerRequest extends CustomerRequest {
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String fullName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,15}", message = "El telefono debe tener entre 7 y 15 dígitos")
    private String phone;

    @NotBlank(message = "La dirección de la empresa es obligatoria")
    private String address;

    private RolCustomer rolCustomer;
    private CustomerStatus customerStatus;

    @Valid
    @NotNull(message = "El representante legal es obligatorio")
    private PersonCustomerRequest legalRepresentative;
}
