package org.example.loficonnect.dto.request.documents;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DocumentSendRequest {
    private String locationId;
    private String documentId;
    private String documentName;
    private String medium;
    private List<CcRecipient> ccRecipients;
    private NotificationSettings notificationSettings;
    private String sentBy;

    @Data
    public static class CcRecipient {
        private String id;
        private String email;
        private String imageUrl;
        private String contactName;
        private String firstName;
        private String lastName;
    }

    @Data
    public static class NotificationSettings {
        private Sender sender;
        private Receive receive;

        @Data
        public static class Sender {
            private String fromName;
            private String fromEmail;
        }

        @Data
        public static class Receive {
            private String subject;
            private String templateId;
        }
    }
}
