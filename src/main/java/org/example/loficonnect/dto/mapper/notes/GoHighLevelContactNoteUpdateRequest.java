package org.example.loficonnect.dto.mapper.notes;

import lombok.Data;
import org.example.loficonnect.dto.request.notes.ContactNoteUpdateRequest;

@Data
public class GoHighLevelContactNoteUpdateRequest {
    private String userId;
    private String body;

    public static GoHighLevelContactNoteUpdateRequest fromRequest(ContactNoteUpdateRequest request) {
        GoHighLevelContactNoteUpdateRequest ghlRequest = new GoHighLevelContactNoteUpdateRequest();
        ghlRequest.setUserId(request.getUserId());
        ghlRequest.setBody(request.getBody());
        return ghlRequest;
    }
}
