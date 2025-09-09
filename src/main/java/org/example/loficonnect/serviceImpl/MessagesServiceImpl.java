package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.messages.*;
import org.example.loficonnect.dto.request.messages.*;
import org.example.loficonnect.feignclients.MessagesClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.MessagesService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MessagesServiceImpl implements MessagesService {
    private final AuthorizationService authorizationService;
    private final MessagesClient messagesClient;

    public MessagesServiceImpl(AuthorizationService authorizationService, MessagesClient messagesClient) {
        this.authorizationService = authorizationService;
        this.messagesClient = messagesClient;
    }

    @Override
    public JsonNode getMessageById(String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return messagesClient.getMessageById(accessKey, version, id);
    }

    @Override
    public JsonNode getMessagesByConversationId(String conversationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return messagesClient.getMessagesByConversationId(accessKey, version, conversationId, queryParams);
    }

    @Override
    public JsonNode sendMessage(MessageCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelMessageCreateRequest ghlRequest = GoHighLevelMessageCreateRequest.fromRequest(request);
        return messagesClient.sendMessage(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode addInboundMessage(InboundMessageCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelInboundMessageCreateRequest ghlRequest = GoHighLevelInboundMessageCreateRequest.fromRequest(request);
        return messagesClient.addInboundMessage(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode addOutboundCall(OutboundCallCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOutboundCallCreateRequest ghlRequest = GoHighLevelOutboundCallCreateRequest.fromRequest(request);
        return messagesClient.addOutboundCall(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode cancelScheduledMessage(String messageId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return messagesClient.cancelScheduledMessage(accessKey, version, messageId);
    }

    @Override
    public JsonNode uploadMessageAttachments(MessagesUploadRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelMessagesUploadRequest ghlRequest = GoHighLevelMessagesUploadRequest.fromRequest(request);
        return messagesClient.uploadMessageAttachments(
                accessKey,
                version,
                ghlRequest.getConversationId(),
                ghlRequest.getLocationId(),
                ghlRequest.getAttachmentUrls()
        );
    }

    @Override
    public JsonNode updateMessageStatus(String messageId, MessageStatusUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelMessageStatusUpdateRequest ghlRequest = GoHighLevelMessageStatusUpdateRequest.fromRequest(request);
        return messagesClient.updateMessageStatus(accessKey, version, messageId, ghlRequest);
    }

    @Override
    public JsonNode getMessageRecording(String messageId, String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return messagesClient.getMessageRecording(accessKey, version, messageId, locationId);
    }

    @Override
    public JsonNode getMessageTranscription(String locationId, String messageId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return messagesClient.getMessageTranscription(accessKey, version, locationId, messageId);
    }

    @Override
    public JsonNode downloadMessageTranscription(String locationId, String messageId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return messagesClient.downloadMessageTranscription(accessKey, version, locationId, messageId);
    }






}
