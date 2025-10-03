package org.example.loficonnect.dto.mapper.integrations;

import lombok.Data;
import org.example.loficonnect.dto.request.integrations.IntegrationCreateRequest;

@Data
public class GoHighLevelIntegrationCreateRequest {
    private String altId;
    private String altType;
    private String uniqueName;
    private String title;
    private String provider;
    private String description;
    private String imageUrl;

    public static GoHighLevelIntegrationCreateRequest fromRequest(IntegrationCreateRequest request) {
        GoHighLevelIntegrationCreateRequest ghlRequest = new GoHighLevelIntegrationCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setUniqueName(request.getUniqueName());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setProvider(request.getProvider());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setImageUrl(request.getImageUrl());

        return ghlRequest;
    }
}
