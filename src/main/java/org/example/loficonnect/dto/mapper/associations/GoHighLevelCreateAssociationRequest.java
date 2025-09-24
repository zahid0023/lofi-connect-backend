package org.example.loficonnect.dto.mapper.associations;

import lombok.Data;
import org.example.loficonnect.dto.request.associations.AssociationCreateRequest;

@Data
public class GoHighLevelCreateAssociationRequest {
    private String locationId;
    private String key;
    private String firstObjectLabel;
    private String firstObjectKey;
    private String secondObjectLabel;
    private String secondObjectKey;

    public static GoHighLevelCreateAssociationRequest fromRequest(AssociationCreateRequest request) {
        GoHighLevelCreateAssociationRequest ghlRequest = new GoHighLevelCreateAssociationRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setKey(request.getKey());
        ghlRequest.setFirstObjectLabel(request.getFirstObjectLabel());
        ghlRequest.setFirstObjectKey(request.getFirstObjectKey());
        ghlRequest.setSecondObjectLabel(request.getSecondObjectLabel());
        ghlRequest.setSecondObjectKey(request.getSecondObjectKey());
        return ghlRequest;
    }
}
