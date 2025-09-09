package org.example.loficonnect.dto.request.messages;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageCreateRequest {
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
    private Long scheduledTimestamp;
    private String conversationProviderId;
    private String emailTo;
    private String emailReplyMode;
    private String fromNumber;
    private String toNumber;
}
