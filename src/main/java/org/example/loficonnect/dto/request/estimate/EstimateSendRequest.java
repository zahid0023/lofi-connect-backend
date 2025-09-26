package org.example.loficonnect.dto.request.estimate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EstimateSendRequest {
    private String altId;
    private String altType;
    private String action;
    private Boolean liveMode;
    private String userId;
    private SentFrom sentFrom;
    private String estimateName;

    @Data
    public static class SentFrom {
        private String fromName;
        private String fromEmail;
    }
}
