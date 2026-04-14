package application.adapters.persistence.request;

import java.sql.Date;

import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest extends PersonRequest {
    @NotNull(message = "El identificador del usuario es obligatorio")
    private long userID;

    private long relatedId;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date birthDate;

    @NotNull(message = "El rol del sistema es obligatorio")
    private RolUser systemRole;

    private UserStatus userStatus;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String userName;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private String company;
}
