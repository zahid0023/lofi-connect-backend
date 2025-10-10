package org.example.loficonnect.dto.request.objectschema;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ObjectSchemaUpdateRequest {
    private Labels labels;
    private String description;
    private String locationId;
    private List<String> searchableProperties;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Labels {
        private String singular;
        private String plural;
    }
}
