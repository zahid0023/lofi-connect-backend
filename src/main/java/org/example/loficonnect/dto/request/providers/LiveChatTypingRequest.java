package org.example.loficonnect.dto.request.providers;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LiveChatTypingRequest {
    private String locationId;
    private Boolean isTyping;
    private String visitorId;
    private String conversationId;
}
