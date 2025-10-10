package org.example.loficonnect.dto.mapper.objectschema;

import lombok.Data;
import org.example.loficonnect.dto.request.objectschema.CustomObjectCreateRequest;

@Data
public class GoHighLevelCreateCustomObjectRequest {
    private CustomObjectCreateRequest.Labels labels;
    private String key;
    private String description;
    private String locationId;
    private CustomObjectCreateRequest.PrimaryDisplayPropertyDetails primaryDisplayPropertyDetails;

    public static GoHighLevelCreateCustomObjectRequest fromRequest(CustomObjectCreateRequest request) {
        GoHighLevelCreateCustomObjectRequest ghlRequest = new GoHighLevelCreateCustomObjectRequest();

        ghlRequest.setLabels(request.getLabels());
        ghlRequest.setKey(request.getKey());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setPrimaryDisplayPropertyDetails(request.getPrimaryDisplayPropertyDetails());

        return ghlRequest;
    }
}
