package org.example.loficonnect.dto.mapper.bulk;

import lombok.Data;
import org.example.loficonnect.dto.request.bulk.BulkBusinessUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelBulkBusinessUpdateRequest {
    private String locationId;
    private List<String> ids;
    private String businessId;

    public static GoHighLevelBulkBusinessUpdateRequest fromRequest(BulkBusinessUpdateRequest request) {
        GoHighLevelBulkBusinessUpdateRequest ghlRequest = new GoHighLevelBulkBusinessUpdateRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setIds(request.getIds());
        ghlRequest.setBusinessId(request.getBusinessId());
        return ghlRequest;
    }
}
