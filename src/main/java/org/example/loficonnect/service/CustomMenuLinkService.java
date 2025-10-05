package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkCreateRequest;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkUpdateRequest;

import java.util.Map;

public interface CustomMenuLinkService {
    JsonNode getCustomMenuLink(String customMenuId);
    JsonNode deleteCustomMenuLink(String customMenuId);
    JsonNode updateCustomMenuLink(String customMenuId, CustomMenuLinkUpdateRequest request);
    JsonNode getCustomMenuLinks(Map<String, Object> queryParams);
    JsonNode createCustomMenuLink(CustomMenuLinkCreateRequest request);
}
