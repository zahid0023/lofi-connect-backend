package org.example.loficonnect.dto.request.messages;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageStatusUpdateRequest {
    private String status;
    private ErrorDetail error;
    private String emailMessageId;
    private List<String> recipients;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorDetail {
        private String code;
        private String type;
        private String message;
    }
}
