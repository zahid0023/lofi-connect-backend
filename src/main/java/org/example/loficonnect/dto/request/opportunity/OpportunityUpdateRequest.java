package org.example.loficonnect.dto.request.opportunity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpportunityUpdateRequest {

    private String pipelineId;
    private String name;
    private String pipelineStageId;
    private String status;
    private Integer monetaryValue;
    private String assignedTo;
    private List<CustomField> customFields;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CustomField {
        private String id;
        private String key;
        private String fieldValue;
    }
}