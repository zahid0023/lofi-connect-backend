package org.example.loficonnect.dto.request.opportunity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpportunityUpsertRequest {
    private String pipeline_id;
    private String location_id;
    private String contact_id;
    private String name;
    private String status;
    private String pipeline_stage_id;
    private Integer monetary_value;
    private String assigned_to;
}
