package org.example.loficonnect.dto.mapper.opportunity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpdateRequest;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelOpportunityUpdateRequest {

    @JsonAlias("pipeline_id")
    private String pipelineId;

    @JsonAlias("name")
    private String name;

    @JsonAlias("pipeline_stage_id")
    private String pipelineStageId;

    @JsonAlias("status")
    private String status;

    @JsonAlias("monetary_value")
    private Integer monetaryValue;

    @JsonAlias("assigned_to")
    private String assignedTo;

    @JsonAlias("custom_fields")
    private List<CustomField> customFields;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomField {
        @JsonAlias("id")
        private String id;

        @JsonAlias("key")
        private String key;

        @JsonAlias("field_value")
        private String field_value;
    }

    private GoHighLevelOpportunityUpdateRequest() {
        // private constructor
    }

    public static GoHighLevelOpportunityUpdateRequest fromRequest(OpportunityUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelOpportunityUpdateRequest.class);
    }
}