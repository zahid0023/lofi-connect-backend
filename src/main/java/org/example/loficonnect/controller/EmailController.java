package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @AppKey
    @GetMapping("/conversations/messages/email/{id}")
    public ResponseEntity<?> getEmailById(@PathVariable("id") String id) {
        return ResponseEntity.ok(emailService.getEmailById(id));
    }

    @AppKey
    @DeleteMapping("/conversations/messages/email/{email-message-id}/schedule")
    public ResponseEntity<?> cancelScheduledEmail(@PathVariable("email-message-id") String emailMessageId) {
        return ResponseEntity.ok(emailService.cancelScheduledEmail(emailMessageId));
    }


}
