package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class CorporateCustomer extends Customer{
    PersonCustomer legalRepresentative;
}
