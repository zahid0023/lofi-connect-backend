package org.example.loficonnect.dto.mapper.documentstemplate;

import lombok.Data;
import org.example.loficonnect.dto.request.documentstemplate.TemplateSendRequest;

@Data
public class GoHighLevelTemplateSendRequest {
    private String templateId;
    private String userId;
    private Boolean sendDocument;
    private String locationId;
    private String contactId;
    private String opportunityId;

    public static GoHighLevelTemplateSendRequest fromRequest(TemplateSendRequest request) {
        GoHighLevelTemplateSendRequest ghl = new GoHighLevelTemplateSendRequest();
        ghl.setTemplateId(request.getTemplateId());
        ghl.setUserId(request.getUserId());
        ghl.setSendDocument(request.getSendDocument());
        ghl.setLocationId(request.getLocationId());
        ghl.setContactId(request.getContactId());
        ghl.setOpportunityId(request.getOpportunityId());
        return ghl;
    }
}
