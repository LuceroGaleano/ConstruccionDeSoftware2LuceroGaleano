package app.domain.models;

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
}
