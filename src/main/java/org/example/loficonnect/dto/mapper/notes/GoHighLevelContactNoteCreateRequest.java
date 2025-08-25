package org.example.loficonnect.dto.mapper.notes;

import lombok.Data;
import org.example.loficonnect.dto.request.notes.ContactNoteCreateRequest;

@Data
public class GoHighLevelContactNoteCreateRequest {
    private String userId;
    private String body;

    public static GoHighLevelContactNoteCreateRequest fromRequest(ContactNoteCreateRequest request) {
        GoHighLevelContactNoteCreateRequest ghlRequest = new GoHighLevelContactNoteCreateRequest();
        ghlRequest.setUserId(request.getUserId());
        ghlRequest.setBody(request.getBody());
        return ghlRequest;
    }
}
