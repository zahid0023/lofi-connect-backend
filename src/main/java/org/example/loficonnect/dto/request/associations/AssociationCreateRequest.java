package org.example.loficonnect.dto.request.associations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssociationCreateRequest {
    private String locationId;
    private String key;
    private String firstObjectLabel;
    private String firstObjectKey;
    private String secondObjectLabel;
    private String secondObjectKey;
}
