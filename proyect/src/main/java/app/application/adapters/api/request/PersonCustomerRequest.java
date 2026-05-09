package app.application.adapters.api.request;

import java.sql.Date;

import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonCustomerRequest  extends CustomerRequest{
    @NotBlank(message = "El nombre completo es obligatorio", groups = OnCreate.class)
    private String fullName;

    @NotBlank(message = "El correo es obligatorio", groups = OnCreate.class)
    @Email(message = "El correo debe tener un formato válido", groups = OnCreate.class)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio", groups = OnCreate.class)
    @Pattern(regexp = "\\d{7,15}", message = "El telefono debe tener entre 7 y 15 dígitos", groups = OnCreate.class)
    private String phone;

    @NotBlank(message = "La dirección es obligatoria", groups = OnCreate.class)
    private String address;

    private RolCustomer rolCustomer;
    private CustomerStatus customerStatus;


    @NotNull(message = "La fecha de nacimiento es obligatoria", groups = OnCreate.class)
    @Past(message = "La fecha de nacimiento debe ser en el pasado", groups = OnCreate.class)
    private Date birthDate;
}