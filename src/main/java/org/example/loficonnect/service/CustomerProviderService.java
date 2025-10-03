package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderCreateRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderDisconnectRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomerProviderCreateRequest;

public interface CustomerProviderService {
    JsonNode createCustomProvider(String locationId, CustomerProviderCreateRequest request);
    JsonNode deleteCustomProvider(String locationId);
    JsonNode getCustomProviderConfig(String locationId);
    JsonNode createCustomProviderConfig(String locationId, CustomProviderCreateRequest request);
    JsonNode disconnectCustomProviderConfig(String locationId, CustomProviderDisconnectRequest request);
}
