package app.application.adapters.api.request;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import app.domain.models.Product;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonCustomerRequest {
    private long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @NotBlank(message = "La identificación es obligatoria")
    private String document;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,15}", message = "El telefono debe tener entre 7 y 15 dígitos")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    private RolCustomer rolCustomer;
    private CustomerStatus customerStatus;

    @Valid
    private List<Product> listProducts = new ArrayList<>();

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date birthDate;
}
