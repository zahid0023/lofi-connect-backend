package org.example.loficonnect.service;

import java.util.Map;

public interface AuthorizationService {
    String generateAuthorizationUrl();

    Map<String, Object> exchangeCodeForToken(String code);
}
