package org.example.loficonnect.dto.mapper.messages;

import lombok.Data;
import org.example.loficonnect.dto.request.messages.InboundMessageCreateRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class GoHighLevelInboundMessageCreateRequest {
    private String type;
    private List<String> attachments;
    private String message;
    private String conversationId;
    private String conversationProviderId;
    private String html;
    private String subject;
    private String emailFrom;
    private String emailTo;
    private List<String> emailCc;
    private List<String> emailBcc;
    private String emailMessageId;
    private String altId;
    private String direction;
    private ZonedDateTime date;
    private Call call;

    @Data
    public static class Call {
        private String to;
        private String from;
        private String status;
    }

    public static GoHighLevelInboundMessageCreateRequest fromRequest(InboundMessageCreateRequest request) {
        GoHighLevelInboundMessageCreateRequest ghlRequest = new GoHighLevelInboundMessageCreateRequest();

        ghlRequest.setType(request.getType());
        ghlRequest.setAttachments(request.getAttachments());
        ghlRequest.setMessage(request.getMessage());
        ghlRequest.setConversationId(request.getConversationId());
        ghlRequest.setConversationProviderId(request.getConversationProviderId());
        ghlRequest.setHtml(request.getHtml());
        ghlRequest.setSubject(request.getSubject());
        ghlRequest.setEmailFrom(request.getEmailFrom());
        ghlRequest.setEmailTo(request.getEmailTo());
        ghlRequest.setEmailCc(request.getEmailCc());
        ghlRequest.setEmailBcc(request.getEmailBcc());
        ghlRequest.setEmailMessageId(request.getEmailMessageId());
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setDirection(request.getDirection());

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
