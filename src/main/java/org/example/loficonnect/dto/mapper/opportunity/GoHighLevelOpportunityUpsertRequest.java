package org.example.loficonnect.dto.mapper.opportunity;

import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpsertRequest;

@Data
public class GoHighLevelOpportunityUpsertRequest {
    private String pipelineId;
    private String locationId;
    private String contactId;
    private String name;
    private String status;
    private String pipelineStageId;
    private Integer monetaryValue;
    private String assignedTo;

    public static GoHighLevelOpportunityUpsertRequest fromRequest(OpportunityUpsertRequest request) {
        GoHighLevelOpportunityUpsertRequest ghl = new GoHighLevelOpportunityUpsertRequest();
        ghl.setPipelineId(request.getPipeline_id());
        ghl.setLocationId(request.getLocation_id());
        ghl.setContactId(request.getContact_id());
        ghl.setName(request.getName());
        ghl.setStatus(request.getStatus());
        ghl.setPipelineStageId(request.getPipeline_stage_id());
        ghl.setMonetaryValue(request.getMonetary_value());
        ghl.setAssignedTo(request.getAssigned_to());
        return ghl;
    }
}
