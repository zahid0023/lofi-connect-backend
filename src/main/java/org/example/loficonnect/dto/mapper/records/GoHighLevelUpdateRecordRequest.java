package org.example.loficonnect.dto.mapper.records;

import lombok.Data;
import org.example.loficonnect.dto.request.records.RecordUpdateRequest;

@Data
public class GoHighLevelUpdateRecordRequest {
    private String key;
    private String description;
    private String locationId;
    private String updatedProperty;

    public static GoHighLevelUpdateRecordRequest fromRequest(RecordUpdateRequest request) {
        GoHighLevelUpdateRecordRequest ghlRequest = new GoHighLevelUpdateRecordRequest();
        ghlRequest.setKey(request.getKey());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setUpdatedProperty(request.getUpdatedProperty());
        return ghlRequest;
    }
}
