package org.example.loficonnect.dto.request.calendar;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationRequest {
    private String type;
    private Boolean shouldSendToContact;
    private Boolean shouldSendToGuest;
    private Boolean shouldSendToUser;
    private Boolean shouldSendToSelectedUsers;
    private String selectedUsers;
}
