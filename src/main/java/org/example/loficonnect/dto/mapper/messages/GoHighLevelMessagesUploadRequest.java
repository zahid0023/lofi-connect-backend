package org.example.loficonnect.dto.mapper.messages;

import lombok.Data;
import org.example.loficonnect.dto.request.messages.MessagesUploadRequest;

import java.util.List;

@Data
public class GoHighLevelMessagesUploadRequest {
    private String conversationId;
    private String locationId;
    private List<String> attachmentUrls;

    public static GoHighLevelMessagesUploadRequest fromRequest(MessagesUploadRequest request) {
        GoHighLevelMessagesUploadRequest ghlRequest = new GoHighLevelMessagesUploadRequest();
        ghlRequest.setConversationId(request.getConversationId());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setAttachmentUrls(request.getAttachmentUrls());
        return ghlRequest;
    }
}
