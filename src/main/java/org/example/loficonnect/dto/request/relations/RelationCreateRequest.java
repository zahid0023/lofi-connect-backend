package org.example.loficonnect.dto.request.relations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RelationCreateRequest {
    private String locationId;
    private String associationId;
    private String firstRecordId;
    private String secondRecordId;
}
