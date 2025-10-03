package org.example.loficonnect.dto.request.customerprovider;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomProviderCreateRequest {
    private Config live;
    private Config test;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Config {
        private String apiKey;
        private String publishableKey;
    }
}
