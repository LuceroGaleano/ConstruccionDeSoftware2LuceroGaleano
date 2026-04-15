package application.adapters.api.response;

import java.sql.Date;

import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;

public record UserResponse(
    String fullName,
    String identification,
    String email,
    String phone,
    String address,
    long userID,
    long relatedId,
    Date birthDate,
    RolUser systemRole,
    UserStatus userStatus,
    String userName,
    String password,
    String company
) {}
