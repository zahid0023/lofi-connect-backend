package org.example.loficonnect.dto.mapper.opportunity;

import lombok.Data;
import org.example.loficonnect.dto.request.opportunity.OpportunityCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelOpportunityCreateRequest {
    private String pipelineId;
    private String locationId;
    private String name;
    private String pipelineStageId;
    private String status;
    private String contactId;
    private Integer monetaryValue;
    private String assignedTo;
    private List<CustomField> customFields;

    @Data
    public static class CustomField {
        private String id;
        private String key;
        private String field_value;
    }

    public static GoHighLevelOpportunityCreateRequest fromRequest(OpportunityCreateRequest request) {
        GoHighLevelOpportunityCreateRequest ghl = new GoHighLevelOpportunityCreateRequest();
        ghl.setPipelineId(request.getPipeline_id());
        ghl.setLocationId(request.getLocation_id());
        ghl.setName(request.getName());
        ghl.setPipelineStageId(request.getPipeline_stage_id());
        ghl.setStatus(request.getStatus());
        ghl.setContactId(request.getContact_id());
        ghl.setMonetaryValue(request.getMonetary_value());
        ghl.setAssignedTo(request.getAssigned_to());

        if (request.getCustom_fields() != null) {
            ghl.setCustomFields(
                request.getCustom_fields().stream().map(f -> {
                    CustomField field = new CustomField();
                    field.setId(f.getId());
                    field.setKey(f.getKey());
                    field.setField_value(f.getField_value());
                    return field;
                }).collect(Collectors.toList())
            );
        }

        return ghl;
    }
}
