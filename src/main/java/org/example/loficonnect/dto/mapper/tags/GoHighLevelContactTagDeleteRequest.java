package org.example.loficonnect.dto.mapper.tags;

import lombok.Data;
import org.example.loficonnect.dto.request.tags.ContactTagDeleteRequest;

import java.util.List;

@Data
public class GoHighLevelContactTagDeleteRequest {
    private List<String> tags;

    public static GoHighLevelContactTagDeleteRequest fromRequest(ContactTagDeleteRequest request) {
        GoHighLevelContactTagDeleteRequest ghlRequest = new GoHighLevelContactTagDeleteRequest();
        ghlRequest.setTags(request.getTags());
        return ghlRequest;
    }
}
