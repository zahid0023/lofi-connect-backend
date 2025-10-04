package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.store.StoreUpdateRequest;

import java.util.Map;

public interface StoreService {
    JsonNode getStoreStats(String storeId, Map<String, Object> queryParams);
    JsonNode updateStoreProducts(String storeId, StoreUpdateRequest request);
}
