package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.oauthgoogle.OauthGoogleBusinessLocationRequest;

import java.util.Map;

public interface OauthGoogleService {
    JsonNode startOauth(Map<String, Object> queryParams);
    JsonNode getGoogleBusinessLocations(String locationId, String accountId);
    JsonNode setGoogleBusinessLocation(String locationId, String accountId, OauthGoogleBusinessLocationRequest request);
}
