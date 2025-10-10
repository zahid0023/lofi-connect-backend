package org.example.loficonnect.dto.mapper.documents;

import lombok.Data;
import org.example.loficonnect.dto.request.documents.DocumentSendRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelDocumentSendRequest {
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

    public static GoHighLevelDocumentSendRequest fromRequest(DocumentSendRequest request) {
        GoHighLevelDocumentSendRequest ghl = new GoHighLevelDocumentSendRequest();
        ghl.setLocationId(request.getLocationId());
        ghl.setDocumentId(request.getDocumentId());
        ghl.setDocumentName(request.getDocumentName());
        ghl.setMedium(request.getMedium());
        ghl.setSentBy(request.getSentBy());

        if (request.getCcRecipients() != null) {
            List<CcRecipient> recipients = request.getCcRecipients().stream().map(r -> {
                CcRecipient cc = new CcRecipient();
                cc.setId(r.getId());
                cc.setEmail(r.getEmail());
                cc.setImageUrl(r.getImageUrl());
                cc.setContactName(r.getContactName());
                cc.setFirstName(r.getFirstName());
                cc.setLastName(r.getLastName());
                return cc;
            }).collect(Collectors.toList());
            ghl.setCcRecipients(recipients);
        }

        if (request.getNotificationSettings() != null) {
            NotificationSettings ns = new NotificationSettings();
            NotificationSettings.Sender sender = new NotificationSettings.Sender();
            sender.setFromName(request.getNotificationSettings().getSender().getFromName());
            sender.setFromEmail(request.getNotificationSettings().getSender().getFromEmail());

            NotificationSettings.Receive receive = new NotificationSettings.Receive();
            receive.setSubject(request.getNotificationSettings().getReceive().getSubject());
            receive.setTemplateId(request.getNotificationSettings().getReceive().getTemplateId());

            ns.setSender(sender);
            ns.setReceive(receive);
            ghl.setNotificationSettings(ns);
        }

        return ghl;
    }
}
