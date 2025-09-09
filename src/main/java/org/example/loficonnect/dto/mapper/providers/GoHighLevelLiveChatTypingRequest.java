package org.example.loficonnect.dto.mapper.providers;

import lombok.Data;
import org.example.loficonnect.dto.request.providers.LiveChatTypingRequest;

@Data
public class GoHighLevelLiveChatTypingRequest {
    private String locationId;
    private Boolean isTyping;
    private String visitorId;
    private String conversationId;

    public static GoHighLevelLiveChatTypingRequest fromRequest(LiveChatTypingRequest request) {
        GoHighLevelLiveChatTypingRequest ghlRequest = new GoHighLevelLiveChatTypingRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setIsTyping(request.getIsTyping());
        ghlRequest.setVisitorId(request.getVisitorId());
        ghlRequest.setConversationId(request.getConversationId());
        return ghlRequest;
    }
}
