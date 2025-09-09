package org.example.loficonnect.dto.mapper.emails;

import lombok.Data;
import org.example.loficonnect.dto.request.emails.EmailTemplateCreateRequest;

@Data
public class GoHighLevelEmailTemplateCreateRequest {
    private String locationId;
    private String title;
    private String type;
    private String updatedBy;
    private String builderVersion;
    private String name;
    private String parentId;
    private String templateDataUrl;
    private String importProvider;
    private String importURL;
    private String templateSource;
    private Boolean isPlainText;

    public static GoHighLevelEmailTemplateCreateRequest fromRequest(EmailTemplateCreateRequest request) {
        GoHighLevelEmailTemplateCreateRequest ghlRequest = new GoHighLevelEmailTemplateCreateRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setType(request.getType());
        ghlRequest.setUpdatedBy(request.getUpdatedBy());
        ghlRequest.setBuilderVersion(request.getBuilderVersion());
        ghlRequest.setName(request.getName());
        ghlRequest.setParentId(request.getParentId());
        ghlRequest.setTemplateDataUrl(request.getTemplateDataUrl());
        ghlRequest.setImportProvider(request.getImportProvider());
        ghlRequest.setImportURL(request.getImportURL());
        ghlRequest.setTemplateSource(request.getTemplateSource());
        ghlRequest.setIsPlainText(request.getIsPlainText());
        return ghlRequest;
    }
}
