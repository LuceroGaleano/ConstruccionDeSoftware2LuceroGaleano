package app.application.adapters.api.response;

import app.domain.models.Customer;
import app.domain.models.enums.ProductCategory;

public record ProductResponse(
    String id,
    String ProductName,
    ProductCategory productCategory,
    Boolean approved,
    Customer customerOwner
) {}
