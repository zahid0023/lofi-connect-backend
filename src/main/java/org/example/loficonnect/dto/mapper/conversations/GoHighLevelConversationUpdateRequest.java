package org.example.loficonnect.dto.mapper.conversations;

import lombok.Data;
import org.example.loficonnect.dto.request.conversations.ConversationUpdateRequest;

import java.util.Map;

@Data
public class GoHighLevelConversationUpdateRequest {
    private String locationId;
    private Integer unreadCount;
    private Boolean starred;
    private Map<String, Object> feedback;

    public static GoHighLevelConversationUpdateRequest fromRequest(ConversationUpdateRequest request) {
        GoHighLevelConversationUpdateRequest ghlRequest = new GoHighLevelConversationUpdateRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setUnreadCount(request.getUnreadCount());
        ghlRequest.setStarred(request.getStarred());
        ghlRequest.setFeedback(request.getFeedback());
        return ghlRequest;
    }
}
