package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.shippingzone.ShippingRateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneCreateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneUpdateRequest;

import java.util.Map;

public interface ShippingZoneService {
    JsonNode createShippingZone(ShippingZoneCreateRequest request);
    JsonNode listShippingZones(Map<String, Object> queryParams);
    JsonNode getShippingZoneById(String shippingZoneId, Map<String, Object> queryParams);
    JsonNode updateShippingZone(String shippingZoneId, ShippingZoneUpdateRequest request);
    JsonNode deleteShippingZone(String shippingZoneId, Map<String, Object> queryParams);
    JsonNode getShippingRates(ShippingRateRequest request);
}
