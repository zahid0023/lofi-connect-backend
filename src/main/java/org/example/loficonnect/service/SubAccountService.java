package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.subaccount.LocationCreateRequest;
import org.example.loficonnect.dto.request.subaccount.LocationUpdateRequest;

import java.util.Map;

public interface SubAccountService {
    JsonNode getLocation(String locationId);

    JsonNode updateLocation(String locationId, LocationUpdateRequest request);

    JsonNode deleteLocation(String locationId, Map<String, Object> queryParams);

    JsonNode createLocation(LocationCreateRequest request);


}
