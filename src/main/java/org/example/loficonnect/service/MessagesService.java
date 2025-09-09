package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.messages.*;

import java.util.Map;

public interface MessagesService {
    JsonNode getMessageById(String id);

    JsonNode getMessagesByConversationId(String conversationId, Map<String, Object> queryParams);

    JsonNode sendMessage(MessageCreateRequest request);

    JsonNode addInboundMessage(InboundMessageCreateRequest request);

    JsonNode addOutboundCall(OutboundCallCreateRequest request);

    JsonNode cancelScheduledMessage(String messageId);

    JsonNode uploadMessageAttachments(MessagesUploadRequest request);

    JsonNode updateMessageStatus(String messageId, MessageStatusUpdateRequest request);

    JsonNode getMessageRecording(String messageId, String locationId);

    JsonNode getMessageTranscription(String locationId, String messageId);

    JsonNode downloadMessageTranscription(String locationId, String messageId);


}
