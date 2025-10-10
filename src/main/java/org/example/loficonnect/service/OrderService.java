package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface OrderService {
    JsonNode getOrders(Map<String, Object> queryParams);
    JsonNode getOrderById(String orderId, Map<String, Object> queryParams);
}
