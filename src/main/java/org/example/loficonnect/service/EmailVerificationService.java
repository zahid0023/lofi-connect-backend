package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.emailverification.EmailVerificationRequest;

import java.util.Map;

public interface EmailVerificationService {
    JsonNode verifyEmail(Map<String, Object> queryParams, EmailVerificationRequest request);
}
