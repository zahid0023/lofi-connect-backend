package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.providers.LiveChatTypingRequest;

public interface ProvidersService {
    JsonNode liveChatTyping(LiveChatTypingRequest request);
}
