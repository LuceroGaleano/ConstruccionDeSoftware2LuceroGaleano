package app.application.adapters.persistence.mongodb.documents;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "users")
public class UserDocument {
    @Id
    private long userId;

    @Indexed(unique = true)
    private String customerDocument;

    private Map<String, List<UserEntry>> users;

    @Getter
    @Setter
    public static class UserEntry {
        private String fullName;
        private String document;
        private String email;
        private String phone;
        private String address;
        private long relatedId;
        private Date birthDate;
        private String systemRole;
        private String userStatus;
        private String userName;
        private String password;
        private String company;
    }
}
