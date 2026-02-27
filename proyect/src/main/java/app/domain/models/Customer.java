package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract  class Customer extends Person {
    RolCustomer rolCustomer;
}
