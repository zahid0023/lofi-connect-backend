package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.providers.LiveChatTypingRequest;
import org.example.loficonnect.service.ProvidersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ghl")
public class ProvidersController {
    private final ProvidersService providersService;

    public ProvidersController(ProvidersService providersService) {
        this.providersService = providersService;
    }

    @AppKey
    @PostMapping("/providers/live-chat/typing")
    public ResponseEntity<?> liveChatTyping(@RequestBody LiveChatTypingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providersService.liveChatTyping(request));
    }

}
