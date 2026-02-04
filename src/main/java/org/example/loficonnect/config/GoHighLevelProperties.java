package org.example.loficonnect.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class GoHighLevelProperties {
    @Value("${GHL_CLIENT_ID}")
    private String clientId;
    @Value("${GHL_CLIENT_SECRET}")
    private String clientSecret;
    @Value("${GHL_BASE_URL}")
    private String baseUrl;
    @Value("${GHL_CODE_URL}")
    private String codeUrl;
    @Value("${GHL_REDIRECT_URI}")
    private String redirectUri;
    @Value("${GHL_TOKEN_URL}")
    private String tokenUrl;
}