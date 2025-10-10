package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.redirect.RedirectCreateRequest;
import org.example.loficonnect.dto.request.redirect.RedirectUpdateRequest;

import java.util.Map;

public interface RedirectService {
    JsonNode createRedirect(RedirectCreateRequest request);
    JsonNode updateRedirect(String id, RedirectUpdateRequest request);
    JsonNode deleteRedirectById(String id, Map<String, Object> queryParams);
    JsonNode getRedirectList(Map<String, Object> queryParams);
}
