package org.example.loficonnect.dto.mapper.messages;

import lombok.Data;
import org.example.loficonnect.dto.request.messages.MessageCreateRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class GoHighLevelMessageCreateRequest {
    private String type;
    private String contactId;
    private String appointmentId;
    private List<String> attachments;
    private String emailFrom;
    private List<String> emailCc;
    private List<String> emailBcc;
    private String html;
    private String message;
    private String subject;
    private String replyMessageId;
    private String templateId;
    private String threadId;
    private ZonedDateTime scheduledTimestamp;
    private String conversationProviderId;
    private String emailTo;
    private String emailReplyMode;
    private String fromNumber;
    private String toNumber;

    public static GoHighLevelMessageCreateRequest fromRequest(MessageCreateRequest request) {
        GoHighLevelMessageCreateRequest ghlRequest = new GoHighLevelMessageCreateRequest();

        ghlRequest.setType(request.getType());
        ghlRequest.setContactId(request.getContactId());
        ghlRequest.setAppointmentId(request.getAppointmentId());
        ghlRequest.setAttachments(request.getAttachments());
        ghlRequest.setEmailFrom(request.getEmailFrom());
        ghlRequest.setEmailCc(request.getEmailCc());
        ghlRequest.setEmailBcc(request.getEmailBcc());
        ghlRequest.setHtml(request.getHtml());
        ghlRequest.setMessage(request.getMessage());
        ghlRequest.setSubject(request.getSubject());
        ghlRequest.setReplyMessageId(request.getReplyMessageId());
        ghlRequest.setTemplateId(request.getTemplateId());
        ghlRequest.setThreadId(request.getThreadId());
        if (request.getScheduledTimestamp() != null) {
            ghlRequest.setScheduledTimestamp(ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(request.getScheduledTimestamp()), ZoneId.of("UTC")));
        }
        ghlRequest.setConversationProviderId(request.getConversationProviderId());
        ghlRequest.setEmailTo(request.getEmailTo());
        ghlRequest.setEmailReplyMode(request.getEmailReplyMode());
        ghlRequest.setFromNumber(request.getFromNumber());
        ghlRequest.setToNumber(request.getToNumber());

        return ghlRequest;
    }
}
