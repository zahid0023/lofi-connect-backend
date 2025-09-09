package org.example.loficonnect.dto.request.messages;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InboundMessageCreateRequest {
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
    private LocalDate date;
    private LocalTime time;
    private CallInfo call;

    @Data
    public static class CallInfo {
        private String to;
        private String from;
        private String status;
    }
}
