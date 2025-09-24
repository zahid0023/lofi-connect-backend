package org.example.loficonnect.dto.mapper.associations;

import lombok.Data;
import org.example.loficonnect.dto.request.associations.AssociationUpdateRequest;

@Data
public class GoHighLevelUpdateAssociationRequest {
    private String firstObjectLabel;
    private String secondObjectLabel;

    public static GoHighLevelUpdateAssociationRequest fromRequest(AssociationUpdateRequest request) {
        GoHighLevelUpdateAssociationRequest ghlRequest = new GoHighLevelUpdateAssociationRequest();
        ghlRequest.setFirstObjectLabel(request.getFirstObjectLabel());
        ghlRequest.setSecondObjectLabel(request.getSecondObjectLabel());
        return ghlRequest;
    }
}
