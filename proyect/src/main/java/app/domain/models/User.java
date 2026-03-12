package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor

public class User extends Person{
    private long userID;
    private long relatedId;
    private String userName;
    private RolUser systemRole;
    private String userStatus;
    private String password;
}
