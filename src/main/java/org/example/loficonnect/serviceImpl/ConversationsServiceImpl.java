package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.conversations.GoHighLevelConversationCreateRequest;
import org.example.loficonnect.dto.mapper.conversations.GoHighLevelConversationUpdateRequest;
import org.example.loficonnect.dto.request.conversations.ConversationCreateRequest;
import org.example.loficonnect.dto.request.conversations.ConversationUpdateRequest;
import org.example.loficonnect.feignclients.ConversationsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ConversationsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConversationsServiceImpl implements ConversationsService {
    private final AuthorizationService authorizationService;
    private final ConversationsClient conversationsClient;

    public ConversationsServiceImpl(AuthorizationService authorizationService, ConversationsClient conversationsClient) {
        this.authorizationService = authorizationService;
        this.conversationsClient = conversationsClient;
    }

    @Override
    public JsonNode createConversation(ConversationCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelConversationCreateRequest ghlRequest = GoHighLevelConversationCreateRequest.fromRequest(request);
        return conversationsClient.createConversation(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getConversation(String conversationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return conversationsClient.getConversation(accessKey, version, conversationId);
    }

    @Override
    public JsonNode updateConversation(String conversationId, ConversationUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelConversationUpdateRequest ghlRequest = GoHighLevelConversationUpdateRequest.fromRequest(request);
        return conversationsClient.updateConversation(accessKey, version, conversationId, ghlRequest);
    }

    @Override
    public JsonNode deleteConversation(String conversationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return conversationsClient.deleteConversation(accessKey, version, conversationId);
    }


}
