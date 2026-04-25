package app.application.adapters.api.response;

import java.util.List;

import app.domain.models.Product;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;

public record CorporateCustomerResponse(
    String fullName,
    String document,
    String email,
    String phone,
    String address,
    RolCustomer rolCustomer,
    CustomerStatus customerStatus,
    List<Product> listProducts,
    PersonCustomerResponse legalRepresentative
) {}
