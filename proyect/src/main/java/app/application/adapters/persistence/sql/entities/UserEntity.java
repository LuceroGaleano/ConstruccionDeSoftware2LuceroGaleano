package app.application.adapters.persistence.sql.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userID;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "document")
    private String document;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "system_role")
    private String systemRole;

    @Column(name = "user_status")
    private String userStatus;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "company")
    private String company;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}