package org.example.loficonnect.dto.mapper.objectschema;

import lombok.Data;
import org.example.loficonnect.dto.request.objectschema.ObjectSchemaUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelUpdateObjectSchemaRequest {
    private ObjectSchemaUpdateRequest.Labels labels;
    private String description;
    private String locationId;
    private List<String> searchableProperties;

    public static GoHighLevelUpdateObjectSchemaRequest fromRequest(ObjectSchemaUpdateRequest request) {
        GoHighLevelUpdateObjectSchemaRequest ghlRequest = new GoHighLevelUpdateObjectSchemaRequest();
        ghlRequest.setLabels(request.getLabels());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setSearchableProperties(request.getSearchableProperties());
        return ghlRequest;
    }
}
