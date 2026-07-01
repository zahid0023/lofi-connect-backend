package org.example.loficonnect.payment.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PaddleRestClientConfig {

    @Bean
    @Qualifier("paddleRestClient")
    public RestClient paddleRestClient(PaddleProperties props) {
        return RestClient.builder()
                .baseUrl(props.getApiBaseUrl())
                .defaultHeader("Authorization", "Bearer " + props.getApiKey())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
