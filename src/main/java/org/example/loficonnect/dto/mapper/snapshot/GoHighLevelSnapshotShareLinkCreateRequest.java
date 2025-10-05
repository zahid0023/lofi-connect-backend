package org.example.loficonnect.dto.mapper.snapshot;

import lombok.Data;
import org.example.loficonnect.dto.request.snapshot.SnapshotShareLinkCreateRequest;

@Data
public class GoHighLevelSnapshotShareLinkCreateRequest {
    private String snapshot_id;
    private String share_type;
    private String relationship_number;
    private String share_location_id;

    public static GoHighLevelSnapshotShareLinkCreateRequest fromRequest(SnapshotShareLinkCreateRequest request) {
        GoHighLevelSnapshotShareLinkCreateRequest ghlRequest = new GoHighLevelSnapshotShareLinkCreateRequest();
        ghlRequest.setSnapshot_id(request.getSnapshotId());
        ghlRequest.setShare_type(request.getShareType());
        ghlRequest.setRelationship_number(request.getRelationshipNumber());
        ghlRequest.setShare_location_id(request.getShareLocationId());
        return ghlRequest;
    }
}
