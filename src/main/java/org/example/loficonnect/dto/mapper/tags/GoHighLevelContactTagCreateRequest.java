package org.example.loficonnect.dto.mapper.tags;

import lombok.Data;
import org.example.loficonnect.dto.request.tags.ContactTagCreateRequest;

import java.util.List;

@Data
public class GoHighLevelContactTagCreateRequest {
    private List<String> tags;

    public static GoHighLevelContactTagCreateRequest fromRequest(ContactTagCreateRequest request) {
        GoHighLevelContactTagCreateRequest ghlRequest = new GoHighLevelContactTagCreateRequest();
        ghlRequest.setTags(request.getTags());
        return ghlRequest;
    }
}
