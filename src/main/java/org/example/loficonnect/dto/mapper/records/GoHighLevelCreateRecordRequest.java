package org.example.loficonnect.dto.mapper.records;

import lombok.Data;
import org.example.loficonnect.dto.request.records.RecordCreateRequest;

@Data
public class GoHighLevelCreateRecordRequest {
    private String key;
    private String description;
    private String locationId;
    private String updatedProperty;

    public static GoHighLevelCreateRecordRequest fromRequest(RecordCreateRequest request) {
        GoHighLevelCreateRecordRequest ghlRequest = new GoHighLevelCreateRecordRequest();
        ghlRequest.setKey(request.getKey());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setUpdatedProperty(request.getUpdatedProperty());
        return ghlRequest;
    }
}
