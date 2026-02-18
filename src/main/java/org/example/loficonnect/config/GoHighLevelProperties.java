package org.example.loficonnect.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ghl")
public class GoHighLevelProperties {
    private String clientId;
    private String clientSecret;
    private String baseUrl;
    private String codeUrl;
    private String redirectUri;
    private String tokenUrl;
}