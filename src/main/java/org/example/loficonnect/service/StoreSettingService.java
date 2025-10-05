package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.storesetting.StoreSettingCreateRequest;

import java.util.Map;

public interface StoreSettingService {
    JsonNode createOrUpdateStoreSetting(StoreSettingCreateRequest request);
    JsonNode getStoreSetting(Map<String, Object> queryParams);
}
