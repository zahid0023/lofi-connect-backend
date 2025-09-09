package org.example.loficonnect.dto.request.conversations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConversationUpdateRequest {
    private String locationId;
    private Integer unreadCount;
    private Boolean starred;
    private Map<String, Object> feedback;
}
