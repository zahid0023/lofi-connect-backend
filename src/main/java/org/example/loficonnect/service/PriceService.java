package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.prices.PriceCreateRequest;
import org.example.loficonnect.dto.request.prices.PriceInventoryUpdateRequest;
import org.example.loficonnect.dto.request.prices.PriceUpdateRequest;

import java.util.Map;

public interface PriceService {
    JsonNode createPrice(String productId, PriceCreateRequest request);
    JsonNode getPrices(String productId, Map<String, Object> queryParams);
    JsonNode listInventory(Map<String, Object> queryParams);
    JsonNode updateInventory(PriceInventoryUpdateRequest request);
    JsonNode getPriceById(String productId, String priceId, Map<String, Object> queryParams);
    JsonNode updatePrice(String productId, String priceId, PriceUpdateRequest request);
    JsonNode deletePrice(String productId, String priceId, Map<String, Object> queryParams);
}
