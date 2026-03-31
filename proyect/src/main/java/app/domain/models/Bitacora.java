package app.domain.models;

import java.util.Date;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor


public class Bitacora {
    long idBitacora;
    String operationType;
    Date operationDate;
    User user;
    Product product;
    String datails;
}
