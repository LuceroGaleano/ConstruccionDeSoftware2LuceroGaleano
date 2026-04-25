package app.domain.models;

import java.sql.Date;

import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor

public class User extends Person{
    private long userID;
    private long relatedId;
    private Date birthDate;
    private RolUser systemRole;
    private UserStatus userStatus;
    private String userName;
    private String password;
    private String company;
    private Customer customer;
}
