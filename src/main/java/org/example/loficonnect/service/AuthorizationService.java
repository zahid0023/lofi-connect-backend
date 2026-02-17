package org.example.loficonnect.service;

import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;

import java.util.List;
import java.util.Map;

public interface AuthorizationService {
    String generateAuthorizationUrl(List<String> scopes, Long apiKeyId);

    Map<String, Object> exchangeCodeForToken(String code);

    void saveGoHighLevelToken(LofiConnectAppKeyEntity entity, Map<String, Object> parameters);

    String getAccessToken(String appKey);

    Map<String, Object> refreshAccessToken(String refreshToken);
}
