package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierCreateRequest;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierUpdateRequest;

import java.util.Map;

public interface ShippingCarrierService {
    JsonNode createShippingCarrier(ShippingCarrierCreateRequest request);
    JsonNode listShippingCarriers(Map<String, Object> queryParams);
    JsonNode getShippingCarrier(String shippingCarrierId, Map<String, Object> queryParams);
    JsonNode updateShippingCarrier(String shippingCarrierId, ShippingCarrierUpdateRequest request);
    JsonNode deleteShippingCarrier(String shippingCarrierId, Map<String, Object> queryParams);
}
