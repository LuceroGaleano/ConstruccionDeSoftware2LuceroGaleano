package app.application.adapters.api.response;

import java.sql.Date;

import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;

public record PersonCustomerResponse(
    long id,
    String fullName,
    String document,
    String email,
    String phone,
    String address,
    RolCustomer rolCustomer,
    CustomerStatus customerStatus,
    Date birthDate
) {}
