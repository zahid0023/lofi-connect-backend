package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.integrations.IntegrationCreateRequest;

import java.util.Map;

public interface IntegrationService {
    JsonNode createIntegration(IntegrationCreateRequest request);
    JsonNode getIntegrationProviders(Map<String, Object> queryParams);
}
