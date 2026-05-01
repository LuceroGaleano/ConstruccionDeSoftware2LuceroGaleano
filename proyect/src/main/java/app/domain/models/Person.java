package app.domain.models;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract class Person {
    private UUID id;
    private String fullName;
    private String document;
    private String email;
    private String phone;
    private String address;
}
