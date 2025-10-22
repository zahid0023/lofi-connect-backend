package org.example.loficonnect.dto.mapper.objectschema;

import lombok.Data;
import org.example.loficonnect.dto.request.objectschema.CustomObjectCreateRequest;

@Data
public class GoHighLevelCreateCustomObjectRequest {
    private Labels labels;
    private String key;
    private String description;
    private String locationId;
    private PrimaryDisplayPropertyDetails primaryDisplayPropertyDetails;

    @Data
    public static class Labels {
        private String singular;
        private String plural;
    }

    @Data
    public static class PrimaryDisplayPropertyDetails {
        private String key;
        private String name;
        private String dataType;
    }

    public static GoHighLevelCreateCustomObjectRequest fromRequest(CustomObjectCreateRequest request) {
        GoHighLevelCreateCustomObjectRequest ghlRequest = new GoHighLevelCreateCustomObjectRequest();

        Labels labels = new Labels();
        labels.setSingular(request.getLabels().getSingular());
        labels.setPlural(request.getLabels().getPlural());

        PrimaryDisplayPropertyDetails details = new PrimaryDisplayPropertyDetails();
        details.setKey(request.getPrimaryDisplayPropertyDetails().getKey());
        details.setName(request.getPrimaryDisplayPropertyDetails().getName());
        details.setDataType(request.getPrimaryDisplayPropertyDetails().getDataType());

        ghlRequest.setLabels(labels);
        ghlRequest.setKey(request.getKey());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setPrimaryDisplayPropertyDetails(details);

        return ghlRequest;
    }
}