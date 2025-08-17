package org.example.loficonnect.dto.mapper.calendarnotifications;

import lombok.Data;
import org.example.loficonnect.dto.request.calendarnotifications.NotificationCreateRequest;

import java.util.List;

@Data
public class GoHighLevelNotificationCreateRequest {
    private String receiverType;
    private String channel;
    private String notificationType;
    private Boolean isActive;
    private String templateId;
    private String body;
    private String subject;
    private List<TimeOffset> afterTime;
    private List<TimeOffset> beforeTime;
    private List<String> additionalEmailIds;
    private List<String> selectedUsers;
    private String fromAddress;
    private String fromName;

    @Data
    public static class TimeOffset {
        private Integer timeOffset;
        private String unit;
    }

    public static GoHighLevelNotificationCreateRequest fromRequest(NotificationCreateRequest request) {
        GoHighLevelNotificationCreateRequest ghlRequest = new GoHighLevelNotificationCreateRequest();

        ghlRequest.setReceiverType(request.getReceiverType());
        ghlRequest.setChannel(request.getChannel());
        ghlRequest.setNotificationType(request.getNotificationType());
        ghlRequest.setIsActive(request.getIsActive());
        ghlRequest.setTemplateId(request.getTemplateId());
        ghlRequest.setBody(request.getBody());
        ghlRequest.setSubject(request.getSubject());
        if (request.getAfterTime() != null) {
            List<TimeOffset> after = request.getAfterTime().stream().map(t -> {
                TimeOffset to = new TimeOffset();
                to.setTimeOffset(t.getTimeOffset());
                to.setUnit(t.getUnit());
                return to;
            }).toList();
            ghlRequest.setAfterTime(after);
        }
        if (request.getBeforeTime() != null) {
            List<TimeOffset> before = request.getBeforeTime().stream().map(t -> {
                TimeOffset to = new TimeOffset();
                to.setTimeOffset(t.getTimeOffset());
                to.setUnit(t.getUnit());
                return to;
            }).toList();
            ghlRequest.setBeforeTime(before);
        }
        ghlRequest.setAdditionalEmailIds(request.getAdditionalEmailIds());
        ghlRequest.setSelectedUsers(request.getSelectedUsers());
        ghlRequest.setFromAddress(request.getFromAddress());
        ghlRequest.setFromName(request.getFromName());

        return ghlRequest;
    }
}
