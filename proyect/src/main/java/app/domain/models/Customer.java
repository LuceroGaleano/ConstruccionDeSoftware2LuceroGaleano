package app.domain.models;

import java.util.ArrayList;

import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract  class Customer extends Person {
    private RolCustomer rolCustomer;
    private CustomerStatus customerStatus;
    private ArrayList<Product> listProducts = new ArrayList<>();
}
