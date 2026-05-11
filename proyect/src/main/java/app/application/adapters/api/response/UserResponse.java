package app.application.adapters.api.response;

import java.sql.Date;
import java.util.UUID;

import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;

public record UserResponse(
    String fullName,
    String document,
    String email,
    String phone,
    String address,
    UUID userID,
    Date birthDate,
    RolUser systemRole,
    UserStatus userStatus,
    String userName,
    String password,
    String company,
    CustomerResponse customer
) {}
