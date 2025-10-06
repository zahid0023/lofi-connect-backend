package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.oauthinstagram.AttachInstagramAccountRequest;

import java.util.Map;

public interface OauthInstagramService {
    JsonNode startOauthInstagram(Map<String, Object> queryParams);
    JsonNode getInstagramProfessionalAccounts(String locationId, String accountId);
    JsonNode attachInstagramAccount(String locationId, String accountId, AttachInstagramAccountRequest request);
}
