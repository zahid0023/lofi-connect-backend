package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface EmailService {
    JsonNode getEmailById(String id);

    JsonNode cancelScheduledEmail(String emailMessageId);
}
