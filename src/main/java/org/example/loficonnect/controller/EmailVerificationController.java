package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.emailverification.EmailVerificationRequest;
import org.example.loficonnect.service.EmailVerificationService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/email-verification")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> verifyEmail(
        @RequestParam("location-id") String locationId,
        @RequestBody EmailVerificationRequest request
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(emailVerificationService.verifyEmail(queryParams, request));
    }
}
