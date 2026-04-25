package app.application.adapters.persistence.sql.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "person_customers")
public class PersonCustomerEntity extends CustomerEntity{
    @Column(name = "birth_date")
    private Date birthDate;
}