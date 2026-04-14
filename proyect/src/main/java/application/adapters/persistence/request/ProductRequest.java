package application.adapters.persistence.request;

import app.domain.models.Customer;
import app.domain.models.enums.ProductCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String productID;

    private String ProductName;

    private ProductCategory productCategory;

    private Boolean approved;

    @Valid
    @NotNull(message = "El propietario del producto es obligatorio")
    private Customer customerOwner;
}
