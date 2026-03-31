package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract class Product {
    private String productID;
    private String ProductName;
    private ProductCategory productCategory;
    private boolean approved;
    private Customer customerOwner;
}
