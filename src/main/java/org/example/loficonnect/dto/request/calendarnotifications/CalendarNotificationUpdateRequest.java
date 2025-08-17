package org.example.loficonnect.dto.request.calendarnotifications;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarNotificationUpdateRequest {
    private String receiverType;
    private List<String> additionalEmailIds;
    private List<String> selectedUsers;
    private String channel;
    private String notificationType;
    private Boolean isActive;
    private Boolean deleted;
    private String templateId;
    private String body;
    private String subject;
    private List<NotificationTimeRequest> afterTime;
    private List<NotificationTimeRequest> beforeTime;
    private String fromAddress;
    private String fromName;

    @Data
    public static class NotificationTimeRequest {
        private Integer timeOffset;
        private String unit;
    }
}
