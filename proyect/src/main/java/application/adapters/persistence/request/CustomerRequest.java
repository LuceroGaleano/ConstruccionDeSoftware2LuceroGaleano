package application.adapters.persistence.request;

import java.util.ArrayList;
import java.util.List;

import app.domain.models.Product;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest extends PersonRequest {
    private RolCustomer rolCustomer;

    private CustomerStatus customerStatus;

    @Valid
    private List<Product> listProducts = new ArrayList<>();
}
