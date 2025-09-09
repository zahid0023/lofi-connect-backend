package org.example.loficonnect.dto.mapper.conversations;

import lombok.Data;
import org.example.loficonnect.dto.request.conversations.ConversationCreateRequest;

@Data
public class GoHighLevelConversationCreateRequest {
    private String locationId;
    private String contactId;

    public static GoHighLevelConversationCreateRequest fromRequest(ConversationCreateRequest request) {
        GoHighLevelConversationCreateRequest ghlRequest = new GoHighLevelConversationCreateRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setContactId(request.getContactId());
        return ghlRequest;
    }
}
