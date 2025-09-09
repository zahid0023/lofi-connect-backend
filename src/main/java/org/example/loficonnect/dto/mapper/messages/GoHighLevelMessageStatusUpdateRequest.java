package org.example.loficonnect.dto.mapper.messages;

import lombok.Data;
import org.example.loficonnect.dto.request.messages.MessageStatusUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelMessageStatusUpdateRequest {
    private String status;
    private Error error;
    private String emailMessageId;
    private List<String> recipients;

    @Data
    public static class Error {
        private String code;
        private String type;
        private String message;
    }

    public static GoHighLevelMessageStatusUpdateRequest fromRequest(MessageStatusUpdateRequest request) {
        GoHighLevelMessageStatusUpdateRequest ghlRequest = new GoHighLevelMessageStatusUpdateRequest();
        ghlRequest.setStatus(request.getStatus());
        if (request.getError() != null) {
            Error error = new Error();
            error.setCode(request.getError().getCode());
            error.setType(request.getError().getType());
            error.setMessage(request.getError().getMessage());
            ghlRequest.setError(error);
        }
        ghlRequest.setEmailMessageId(request.getEmailMessageId());
        ghlRequest.setRecipients(request.getRecipients());
        return ghlRequest;
    }
}
