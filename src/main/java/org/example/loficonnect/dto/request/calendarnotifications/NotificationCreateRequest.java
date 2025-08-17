package org.example.loficonnect.dto.request.calendarnotifications;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationCreateRequest {
    private String receiverType;
    private String channel;
    private String notificationType;
    private Boolean isActive;
    private String templateId;
    private String body;
    private String subject;
    private List<TimeOffsetRequest> afterTime;
    private List<TimeOffsetRequest> beforeTime;
    private List<String> additionalEmailIds;
    private List<String> selectedUsers;
    private String fromAddress;
    private String fromName;

    @Data
    public static class TimeOffsetRequest {
        private Integer timeOffset;
        private String unit;
    }
}
