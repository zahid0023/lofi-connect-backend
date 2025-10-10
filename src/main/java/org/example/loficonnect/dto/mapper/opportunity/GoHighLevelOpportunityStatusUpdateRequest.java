package org.example.loficonnect.dto.mapper.opportunity;

import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityStatusUpdateRequest;

@Data
public class GoHighLevelOpportunityStatusUpdateRequest {
    private String status;

    public static GoHighLevelOpportunityStatusUpdateRequest fromRequest(OpportunityStatusUpdateRequest request) {
        GoHighLevelOpportunityStatusUpdateRequest ghl = new GoHighLevelOpportunityStatusUpdateRequest();
        ghl.setStatus(request.getStatus());
        return ghl;
    }
}
