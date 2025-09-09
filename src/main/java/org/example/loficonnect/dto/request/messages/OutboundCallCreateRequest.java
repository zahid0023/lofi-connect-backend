package org.example.loficonnect.dto.request.messages;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutboundCallCreateRequest {
    private String type;
    private List<String> attachments;
    private String conversationId;
    private String conversationProviderId;
    private String altId;
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
