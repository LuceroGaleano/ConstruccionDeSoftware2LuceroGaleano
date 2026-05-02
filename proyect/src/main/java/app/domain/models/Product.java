package app.domain.models;

import java.util.UUID;

import app.domain.models.enums.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract class Product {
    private UUID id;
    private String ProductName;
    private ProductCategory productCategory;
    private boolean approved;
    private Customer customerOwner;
}
