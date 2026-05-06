package app.domain.models;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import app.domain.models.enums.OperationBitacora;
import app.domain.models.enums.RolUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor


public class Bitacora {
    String id;
    OperationBitacora operationType;
    Date operationDate;
    String userDocument;
    RolUser rolUser;
    UUID productId;
    Map<String, Object> detailData;
}
