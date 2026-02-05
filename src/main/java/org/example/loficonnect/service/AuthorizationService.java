package org.example.loficonnect.service;

import org.example.loficonnect.dto.response.AppKeyResponse;

import java.util.List;
import java.util.Map;

public interface AuthorizationService {
    String generateAuthorizationUrl(List<String> scopes);

    Map<String, Object> exchangeCodeForToken(String code);

    void generateAndSaveAppKey(Map<String, Object> parameters, String code);

    String getAccessToken(String appKey);

    Map<String, Object> refreshAccessToken(String refreshToken);

    AppKeyResponse activateAppKey(String code, String connectionName);
}
