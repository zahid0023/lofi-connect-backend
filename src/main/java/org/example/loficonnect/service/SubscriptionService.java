package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface SubscriptionService {
    JsonNode getSubscriptions(Map<String, Object> queryParams);
    JsonNode getSubscriptionById(String subscriptionId, Map<String, Object> queryParams);
}
