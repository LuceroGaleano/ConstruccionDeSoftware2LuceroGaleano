package app.application.adapters.api.response;

import java.util.UUID;

import app.domain.models.Customer;
import app.domain.models.enums.ProductCategory;

public record ProductResponse(
    UUID id,
    String ProductName,
    ProductCategory productCategory,
    Boolean approved,
    Customer customerOwner
) {}
