package org.example.loficonnect.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLoggingConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        // FULL will log headers, body, and metadata
        return Logger.Level.FULL;
    }
}
