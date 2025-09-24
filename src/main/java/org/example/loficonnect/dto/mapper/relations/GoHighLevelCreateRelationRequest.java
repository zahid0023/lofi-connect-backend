package org.example.loficonnect.dto.mapper.relations;

import lombok.Data;
import org.example.loficonnect.dto.request.relations.RelationCreateRequest;

@Data
public class GoHighLevelCreateRelationRequest {
    private String locationId;
    private String associationId;
    private String firstRecordId;
    private String secondRecordId;

    public static GoHighLevelCreateRelationRequest fromRequest(RelationCreateRequest request) {
        GoHighLevelCreateRelationRequest ghlRequest = new GoHighLevelCreateRelationRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setAssociationId(request.getAssociationId());
        ghlRequest.setFirstRecordId(request.getFirstRecordId());
        ghlRequest.setSecondRecordId(request.getSecondRecordId());
        return ghlRequest;
    }
}
