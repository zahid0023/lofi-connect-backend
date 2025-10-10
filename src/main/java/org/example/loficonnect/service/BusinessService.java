package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.businesses.BusinessCreateRequest;
import org.example.loficonnect.dto.request.businesses.BusinessUpdateRequest;

import java.util.Map;

public interface BusinessService {
    JsonNode getBusiness(String businessId);
    JsonNode createBusiness(BusinessCreateRequest request);
    JsonNode getBusinessesByLocation(Map<String, Object> queryParams);
    JsonNode updateBusiness(String businessId, BusinessUpdateRequest request);
    JsonNode deleteBusiness(String businessId);
}
