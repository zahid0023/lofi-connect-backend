package org.example.loficonnect.dto.mapper.opportunity;

import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelOpportunityUpdateRequest {
    private String pipelineId;
    private String name;
    private String pipelineStageId;
    private String status;
    private Integer monetaryValue;
    private String assignedTo;
    private List<CustomField> customFields;

    @Data
    public static class CustomField {
        private String id;
        private String key;
        private String field_value;
    }

    public static GoHighLevelOpportunityUpdateRequest fromRequest(OpportunityUpdateRequest request) {
        GoHighLevelOpportunityUpdateRequest ghlRequest = new GoHighLevelOpportunityUpdateRequest();
        ghlRequest.setPipelineId(request.getPipeline_id());
        ghlRequest.setName(request.getName());
        ghlRequest.setPipelineStageId(request.getPipeline_stage_id());
        ghlRequest.setStatus(request.getStatus());
        ghlRequest.setMonetaryValue(request.getMonetary_value());
        ghlRequest.setAssignedTo(request.getAssigned_to());

        if (request.getCustom_fields() != null) {
            ghlRequest.setCustomFields(
                request.getCustom_fields().stream().map(cf -> {
                    CustomField f = new CustomField();
                    f.setId(cf.getId());
                    f.setKey(cf.getKey());
                    f.setField_value(cf.getField_value());
                    return f;
                }).collect(Collectors.toList())
            );
        }

        return ghlRequest;
    }
}
