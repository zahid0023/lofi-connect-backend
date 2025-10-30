package org.example.loficonnect.dto.mapper.opportunity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityStatusUpdateRequest;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelOpportunityStatusUpdateRequest {

    @JsonAlias("status")
    private String status;

    private GoHighLevelOpportunityStatusUpdateRequest() {
        // private constructor
    }

    public static GoHighLevelOpportunityStatusUpdateRequest fromRequest(OpportunityStatusUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelOpportunityStatusUpdateRequest.class);
    }
}