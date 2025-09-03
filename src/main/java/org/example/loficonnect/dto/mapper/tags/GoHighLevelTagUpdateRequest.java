package org.example.loficonnect.dto.mapper.tags;

import lombok.Data;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;

@Data
public class GoHighLevelTagUpdateRequest {
    private String name;

    public static GoHighLevelTagUpdateRequest fromRequest(TagUpdateRequest request) {
        GoHighLevelTagUpdateRequest ghlRequest = new GoHighLevelTagUpdateRequest();
        ghlRequest.setName(request.getName());
        return ghlRequest;
    }
}
