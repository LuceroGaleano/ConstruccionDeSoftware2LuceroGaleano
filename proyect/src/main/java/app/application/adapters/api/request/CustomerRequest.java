package app.application.adapters.api.request;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank(message = "La identificación es obligatoria", groups = {OnCreate.class})
    private String document;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Date birthDate;
    private CustomerRequest legalRepresentative;
}