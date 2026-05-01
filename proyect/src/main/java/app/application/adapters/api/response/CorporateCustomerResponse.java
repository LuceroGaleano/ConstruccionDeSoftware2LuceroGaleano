package app.application.adapters.api.response;

import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;

public record CorporateCustomerResponse(
    long id,
    String fullName,
    String document,
    String email,
    String phone,
    String address,
    RolCustomer rolCustomer,
    CustomerStatus customerStatus,
    PersonCustomerResponse legalRepresentative
) {}
