package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateCreateRequest;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateUpdateRequest;

import java.util.Map;

public interface ShippingZoneRateService {
    JsonNode createShippingZoneRate(String shippingZoneId, ShippingZoneRateCreateRequest request);
    JsonNode listShippingZoneRates(String shippingZoneId, Map<String, Object> queryParams);
    JsonNode getShippingZoneRateById(String shippingZoneId, String shippingRateId, Map<String, Object> queryParams);
    JsonNode updateShippingZoneRate(String shippingZoneId, String shippingRateId, ShippingZoneRateUpdateRequest request);
    JsonNode deleteShippingZoneRate(String shippingZoneId, String shippingRateId, Map<String, Object> queryParams);

}
