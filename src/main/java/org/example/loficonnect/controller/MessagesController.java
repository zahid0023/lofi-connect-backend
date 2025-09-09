package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.messages.*;
import org.example.loficonnect.service.MessagesService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MessagesController {
    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @AppKey
    @GetMapping("/conversations/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable("id") String id) {
        return ResponseEntity.ok(messagesService.getMessageById(id));
    }

    @AppKey
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<?> getMessagesByConversationId(
            @PathVariable("conversationId") String conversationId,
            @RequestParam(value = "last-message-id", required = false) String lastMessageId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "type", required = false) String type
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "lastMessageId", lastMessageId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "type", type);

        return ResponseEntity.ok(messagesService.getMessagesByConversationId(conversationId, queryParams));
    }

    @AppKey
    @PostMapping("/conversations/messages")
    public ResponseEntity<?> sendMessage(@RequestBody MessageCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messagesService.sendMessage(request));
    }

    @AppKey
    @PostMapping("/conversations/messages/inbound")
    public ResponseEntity<?> addInboundMessage(@RequestBody InboundMessageCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messagesService.addInboundMessage(request));
    }

    @AppKey
    @PostMapping("/conversations/messages/outbound")
    public ResponseEntity<?> addOutboundCall(@RequestBody OutboundCallCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messagesService.addOutboundCall(request));
    }

    @AppKey
    @DeleteMapping("/conversations/messages/{messageId}/schedule")
    public ResponseEntity<?> cancelScheduledMessage(@PathVariable("messageId") String messageId) {
        return ResponseEntity.ok(messagesService.cancelScheduledMessage(messageId));
    }

    @AppKey
    @PostMapping("/messages/upload")
    public ResponseEntity<?> uploadMessageAttachments(@ModelAttribute MessagesUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messagesService.uploadMessageAttachments(request));
    }

    @AppKey
    @PutMapping("/messages/{messageId}/status")
    public ResponseEntity<?> updateMessageStatus(
            @PathVariable("messageId") String messageId,
            @RequestBody MessageStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(messagesService.updateMessageStatus(messageId, request));
    }

    @AppKey
    @GetMapping("/messages/{messageId}/locations/{locationId}/recording")
    public ResponseEntity<?> getMessageRecording(
            @PathVariable("messageId") String messageId,
            @PathVariable("locationId") String locationId
    ) {
        return ResponseEntity.ok(messagesService.getMessageRecording(messageId, locationId));
    }

    @AppKey
    @GetMapping("/locations/{locationId}/messages/{messageId}/transcription")
    public ResponseEntity<?> getMessageTranscription(
            @PathVariable("locationId") String locationId,
            @PathVariable("messageId") String messageId
    ) {
        return ResponseEntity.ok(messagesService.getMessageTranscription(locationId, messageId));
    }

    @AppKey
    @GetMapping("/locations/{locationId}/messages/{messageId}/transcription/download")
    public ResponseEntity<?> downloadMessageTranscription(
            @PathVariable("locationId") String locationId,
            @PathVariable("messageId") String messageId
    ) {
        return ResponseEntity.ok(messagesService.downloadMessageTranscription(locationId, messageId));
    }





}
