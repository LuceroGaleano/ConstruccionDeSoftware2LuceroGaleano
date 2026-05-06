package app.application.adapters.persistence.mongodb.documents;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "bitacora")
public class BitacoraDocument {

    @Id
    private String id;

    private String operationType;

    private Date operationDate;

    private String userDocument;

    private String rolUser;

    private UUID productId;

    private Map<String, Object> detailData;
}