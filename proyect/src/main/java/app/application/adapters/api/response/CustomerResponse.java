package app.application.adapters.api.response;

import java.util.UUID;

import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.RolCustomer;

public record CustomerResponse(
    UUID id,
    String fullName,
    String document,
    String email,
    String phone,
    String address,
    RolCustomer rolCustomer,
    CustomerStatus customerStatus
) {}
