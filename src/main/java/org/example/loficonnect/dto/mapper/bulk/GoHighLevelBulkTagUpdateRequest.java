package org.example.loficonnect.dto.mapper.bulk;

import lombok.Data;
import org.example.loficonnect.dto.request.bulk.BulkTagUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelBulkTagUpdateRequest {
    private List<String> contacts;
    private List<String> tags;
    private String locationId;
    private Boolean removeAllTags;

    public static GoHighLevelBulkTagUpdateRequest fromRequest(BulkTagUpdateRequest request) {
        GoHighLevelBulkTagUpdateRequest ghlRequest = new GoHighLevelBulkTagUpdateRequest();
        ghlRequest.setContacts(request.getContacts());
        ghlRequest.setTags(request.getTags());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setRemoveAllTags(request.getRemoveAllTags());
        return ghlRequest;
    }
}
