package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.conversations.ConversationCreateRequest;
import org.example.loficonnect.dto.request.conversations.ConversationUpdateRequest;

public interface ConversationsService {
    JsonNode createConversation(ConversationCreateRequest request);

    JsonNode getConversation(String conversationId);

    JsonNode updateConversation(String conversationId, ConversationUpdateRequest request);

    JsonNode deleteConversation(String conversationId);


}
