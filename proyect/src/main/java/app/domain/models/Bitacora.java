package app.domain.models;

import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor


public class Bitacora {
    String id;
    String operationType;
    Date operationDate;
    User user;
    Product product;
    Map<String, Object> detailData;
}
