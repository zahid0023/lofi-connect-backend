package org.example.loficonnect.dto.mapper.estimate;

import lombok.Data;
import org.example.loficonnect.dto.request.estimate.LastVisitedUpdateRequest;

@Data
public class GoHighLevelLastVisitedUpdateRequest {
    private String estimateId;

    public static GoHighLevelLastVisitedUpdateRequest fromRequest(LastVisitedUpdateRequest request) {
        GoHighLevelLastVisitedUpdateRequest ghlRequest = new GoHighLevelLastVisitedUpdateRequest();
        ghlRequest.setEstimateId(request.getEstimateId());
        return ghlRequest;
    }
}
