package org.example.loficonnect.dto.mapper.calendarnotifications;

import lombok.Data;
import org.example.loficonnect.dto.request.calendarnotifications.CalendarNotificationUpdateRequest;
import java.util.stream.Collectors;

@Data
public class GoHighLevelCalendarNotificationUpdateRequest {
    private String receiverType;
    private java.util.List<String> additionalEmailIds;
    private java.util.List<String> selectedUsers;
    private String channel;
    private String notificationType;
    private Boolean isActive;
    private Boolean deleted;
    private String templateId;
    private String body;
    private String subject;
    private java.util.List<TimeBlock> afterTime;
    private java.util.List<TimeBlock> beforeTime;
    private String fromAddress;
    private String fromName;

    @Data
    public static class TimeBlock {
        private Integer timeOffset;
        private String unit;
    }

    public static GoHighLevelCalendarNotificationUpdateRequest fromRequest(CalendarNotificationUpdateRequest request) {
        GoHighLevelCalendarNotificationUpdateRequest ghlRequest = new GoHighLevelCalendarNotificationUpdateRequest();

        ghlRequest.setReceiverType(request.getReceiverType());
        ghlRequest.setAdditionalEmailIds(request.getAdditionalEmailIds());
        ghlRequest.setSelectedUsers(request.getSelectedUsers());
        ghlRequest.setChannel(request.getChannel());
        ghlRequest.setNotificationType(request.getNotificationType());
        ghlRequest.setIsActive(request.getIsActive());
        ghlRequest.setDeleted(request.getDeleted());
        ghlRequest.setTemplateId(request.getTemplateId());
        ghlRequest.setBody(request.getBody());
        ghlRequest.setSubject(request.getSubject());
        ghlRequest.setFromAddress(request.getFromAddress());
        ghlRequest.setFromName(request.getFromName());

        if(request.getAfterTime() != null) {
            ghlRequest.setAfterTime(request.getAfterTime().stream().map(t -> {
                TimeBlock tb = new TimeBlock();
                tb.setTimeOffset(t.getTimeOffset());
                tb.setUnit(t.getUnit());
                return tb;
            }).collect(Collectors.toList()));
        }

        if(request.getBeforeTime() != null) {
            ghlRequest.setBeforeTime(request.getBeforeTime().stream().map(t -> {
                TimeBlock tb = new TimeBlock();
                tb.setTimeOffset(t.getTimeOffset());
                tb.setUnit(t.getUnit());
                return tb;
            }).collect(Collectors.toList()));
        }

        return ghlRequest;
    }
}
