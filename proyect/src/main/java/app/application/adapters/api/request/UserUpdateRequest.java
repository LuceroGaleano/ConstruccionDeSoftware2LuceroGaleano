package app.application.adapters.api.request;

import java.sql.Date;
import java.util.UUID;

import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private UUID relatedId;
    private Date birthDate;
    private RolUser systemRole;
    private UserStatus userStatus;
    private String userName;
    private String password;
    private String company;
        private CustomerRequest customer;
}