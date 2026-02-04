package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.messages.GoHighLevelInboundMessageCreateRequest;
import org.example.loficonnect.dto.mapper.messages.GoHighLevelMessageCreateRequest;
import org.example.loficonnect.dto.mapper.messages.GoHighLevelMessageStatusUpdateRequest;
import org.example.loficonnect.dto.mapper.messages.GoHighLevelOutboundCallCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "messageClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface MessagesClient {
    @GetMapping(
            value = "/conversations/messages/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getMessageById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String id
    );

    @GetMapping(
            value = "/conversations/{conversationId}/messages",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getMessagesByConversationId(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("conversationId") String conversationId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/conversations/messages",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode sendMessage(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelMessageCreateRequest request
    );

    @PostMapping(
            value = "/conversations/messages/inbound",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode addInboundMessage(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelInboundMessageCreateRequest request
    );

    @PostMapping(
            value = "/conversations/messages/outbound",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode addOutboundCall(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelOutboundCallCreateRequest request
    );

    @DeleteMapping(
            value = "/conversations/messages/{messageId}/schedule",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode cancelScheduledMessage(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("messageId") String messageId
    );

    @PostMapping(
            value = "/conversations/messages/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode uploadMessageAttachments(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestPart("conversationId") String conversationId,
            @RequestPart(value = "locationId", required = false) String locationId,
            @RequestPart(value = "attachmentUrls", required = false) List<String> attachmentUrls
    );

    @PutMapping(
            value = "/conversations/messages/{messageId}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateMessageStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("messageId") String messageId,
            @RequestBody GoHighLevelMessageStatusUpdateRequest request
    );

    @GetMapping(
            value = "/conversations/messages/{messageId}/locations/{locationId}/recording",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getMessageRecording(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("messageId") String messageId,
            @PathVariable("locationId") String locationId
    );

    @GetMapping(
            value = "/conversations/locations/{locationId}/messages/{messageId}/transcription",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getMessageTranscription(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("messageId") String messageId
    );

    @GetMapping(
            value = "/conversations/locations/{locationId}/messages/{messageId}/transcription/download",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode downloadMessageTranscription(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("messageId") String messageId
    );


}
