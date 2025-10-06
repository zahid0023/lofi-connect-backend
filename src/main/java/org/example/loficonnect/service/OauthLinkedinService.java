package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.oauthlinkedin.AttachLinkedinAccountRequest;

import java.util.Map;

public interface OauthLinkedinService {
    JsonNode startOauthLinkedin(Map<String, Object> queryParams);
    JsonNode getLinkedinAccounts(String locationId, String accountId);
    JsonNode attachLinkedinAccount(String locationId, String accountId, AttachLinkedinAccountRequest request);
}
