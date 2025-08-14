package org.example.loficonnect.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "gohighlevel")
public class GoHighLevelProperties {
    private String clientId;
    private String clientSecret;
    private String baseUrl;
    private String codeUrl;
    private String redirectUri;
    private String tokenUrl;
    private List<String> scopes;
}