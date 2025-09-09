package org.example.loficonnect.dto.mapper.messages;

import lombok.Data;
import org.example.loficonnect.dto.request.messages.OutboundCallCreateRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class GoHighLevelOutboundCallCreateRequest {
    private String type;
    private List<String> attachments;
    private String conversationId;
    private String conversationProviderId;
    private String altId;
    private ZonedDateTime date;
    private Call call;

    @Data
    public static class Call {
        private String to;
        private String from;
        private String status;
    }

    public static GoHighLevelOutboundCallCreateRequest fromRequest(OutboundCallCreateRequest request) {
        GoHighLevelOutboundCallCreateRequest ghlRequest = new GoHighLevelOutboundCallCreateRequest();

        ghlRequest.setType(request.getType());
        ghlRequest.setAttachments(request.getAttachments());
        ghlRequest.setConversationId(request.getConversationId());
        ghlRequest.setConversationProviderId(request.getConversationProviderId());
        ghlRequest.setAltId(request.getAltId());

        if (request.getDate() != null && request.getTime() != null) {
            LocalDateTime dateTime = LocalDateTime.of(request.getDate(), request.getTime());
            ghlRequest.setDate(ZonedDateTime.of(dateTime, ZoneId.of("UTC")));
        }

        if (request.getCall() != null) {
            Call call = new Call();
            call.setTo(request.getCall().getTo());
            call.setFrom(request.getCall().getFrom());
            call.setStatus(request.getCall().getStatus());
            ghlRequest.setCall(call);
        }

        return ghlRequest;
    }
}
