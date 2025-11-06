package org.example.loficonnect.dto.mapper.opportunity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpsertRequest;
import org.example.loficonnect.util.LocationContext;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelOpportunityUpsertRequest {

    @JsonAlias("pipeline_id")
    private String pipelineId;

    private String locationId;

    @JsonAlias("contact_id")
    private String contactId;

    @JsonAlias("name")
    private String name;

    @JsonAlias("status")
    private String status;

    @JsonAlias("pipeline_stage_id")
    private String pipelineStageId;

    @JsonAlias("monetary_value")
    private Integer monetaryValue;

    @JsonAlias("assigned_to")
    private String assignedTo;

    private GoHighLevelOpportunityUpsertRequest() {
        // private constructor
    }

    public static GoHighLevelOpportunityUpsertRequest fromRequest(OpportunityUpsertRequest request, ObjectMapper objectMapper) {
        GoHighLevelOpportunityUpsertRequest ghl = objectMapper.convertValue(request, GoHighLevelOpportunityUpsertRequest.class);
        ghl.setLocationId(LocationContext.getLocationId());
        return ghl;
    }
}