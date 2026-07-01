package org.example.loficonnect.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "paddle")
public class PaddleProperties {

    /**
     * Paddle API secret key (Bearer token).
     */
    private String apiKey;

    /**
     * Paddle webhook secret used for HMAC-SHA256 signature verification.
     */
    private String webhookSecret;

    /**
     * Paddle REST API base URL. Defaults to production; set to sandbox URL for testing.
     */
    private String apiBaseUrl;

    /**
     * URL Paddle redirects the browser to after checkout.
     * Should point to {@code {backend-url}/api/v1/subscriptions/tenant-subscriptions/success}.
     * Configured via {@code PADDLE_SUCCESS_URL} env var, falling back to the backend URL.
     */
    private String successUrl;

}
