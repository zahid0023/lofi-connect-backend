package org.example.loficonnect.dto.request.opportunity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpportunityUpsertRequest {
    private String pipelineId;
    private String contactId;
    private String name;
    private String status;
    private String pipelineStageId;
    private Integer monetaryValue;
    private String assignedTo;
}
