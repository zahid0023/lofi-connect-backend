package org.example.loficonnect.dto.request.records;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecordCreateRequest {
    private String key;
    private String description;
    private String locationId;
    private String updatedProperty;
}
