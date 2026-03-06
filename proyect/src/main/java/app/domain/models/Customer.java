package app.domain.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract  class Customer extends Person {
    private RolCustomer rolCustomer;
    private ArrayList<Product> listProducts = new ArrayList<>();
}
