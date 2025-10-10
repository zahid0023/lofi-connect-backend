package org.example.loficonnect.dto.request.opportunity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpportunityUpdateRequest {
    private String pipeline_id;
    private String name;
    private String pipeline_stage_id;
    private String status;
    private Integer monetary_value;
    private String assigned_to;
    private List<CustomField> custom_fields;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CustomField {
        private String id;
        private String key;
        private String field_value;
    }
}
