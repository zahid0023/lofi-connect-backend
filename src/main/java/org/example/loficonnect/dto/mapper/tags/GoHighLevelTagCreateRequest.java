package org.example.loficonnect.dto.mapper.tags;

import lombok.Data;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;

@Data
public class GoHighLevelTagCreateRequest {
    private String name;

    public static GoHighLevelTagCreateRequest fromRequest(TagCreateRequest request) {
        GoHighLevelTagCreateRequest ghlRequest = new GoHighLevelTagCreateRequest();
        ghlRequest.setName(request.getName());
        return ghlRequest;
    }
}
