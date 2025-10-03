package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.orderfulfillment.OrderFulfillmentCreateRequest;

import java.util.Map;

public interface OrderFulfillmentService {
    JsonNode createFulfillment(String orderId, OrderFulfillmentCreateRequest request);
    JsonNode getFulfillments(String orderId, Map<String, Object> queryParams);
}
