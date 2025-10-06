package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.oauthtiktok.OauthtiktokAttachRequest;

import java.util.Map;

public interface OauthtiktokService {
    JsonNode startTiktokOAuth(Map<String, Object> queryParams);
    JsonNode getTiktokProfile(String locationId, String accountId);
    JsonNode attachTiktokProfile(String locationId, String accountId, OauthtiktokAttachRequest request);
    JsonNode startTiktokBusinessOAuth(Map<String, Object> queryParams);
    JsonNode getTiktokBusinessProfile(String locationId, String accountId);
}
