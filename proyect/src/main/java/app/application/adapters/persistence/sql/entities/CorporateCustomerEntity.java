package app.application.adapters.persistence.sql.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "corporate_customers")
public class CorporateCustomerEntity extends CustomerEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "legal_representative_id")
    private PersonCustomerEntity legalRepresentative;
}