package app.application.adapters.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank(message = "La identificación es obligatoria", groups = {OnCreate.class, OnSearch.class})
    private String document;

}
