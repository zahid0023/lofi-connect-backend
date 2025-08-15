package org.example.loficonnect.service;

import org.example.loficonnect.dto.response.AppKeyResponse;

import java.util.Map;

public interface AuthorizationService {
    String generateAuthorizationUrl();

    Map<String, Object> exchangeCodeForToken(String code);

    AppKeyResponse generateAndSaveAppKey(Map<String, Object> parameters);
}
