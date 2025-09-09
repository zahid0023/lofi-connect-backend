package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.conversations.ConversationCreateRequest;
import org.example.loficonnect.dto.request.conversations.ConversationUpdateRequest;
import org.example.loficonnect.service.ConversationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ConversationsController {
    private final ConversationsService conversationsService;

    public ConversationsController(ConversationsService conversationsService) {
        this.conversationsService = conversationsService;
    }

    @AppKey
    @PostMapping("/conversations")
    public ResponseEntity<?> createConversation(@RequestBody ConversationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversationsService.createConversation(request));
    }

    @AppKey
    @GetMapping("/conversations/{conversation-id}")
    public ResponseEntity<?> getConversation(@PathVariable("conversation-id") String conversationId) {
        return ResponseEntity.ok(conversationsService.getConversation(conversationId));
    }

    @AppKey
    @PutMapping("/conversations/{conversation-id}")
    public ResponseEntity<?> updateConversation(
            @PathVariable("conversation-id") String conversationId,
            @RequestBody ConversationUpdateRequest request) {
        return ResponseEntity.ok(conversationsService.updateConversation(conversationId, request));
    }

    @AppKey
    @DeleteMapping("/conversations/{conversation-id}")
    public ResponseEntity<?> deleteConversation(@PathVariable("conversation-id") String conversationId) {
        return ResponseEntity.ok(conversationsService.deleteConversation(conversationId));
    }


}
