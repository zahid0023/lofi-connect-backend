package org.example.loficonnect.dto.request.objectschema;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomObjectCreateRequest {
    private Labels labels;
    private String key;
    private String description;
    private String locationId;
    private PrimaryDisplayPropertyDetails primaryDisplayPropertyDetails;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Labels {
        private String singular;
        private String plural;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PrimaryDisplayPropertyDetails {
        private String key;
        private String name;
        private String dataType;
    }
}
