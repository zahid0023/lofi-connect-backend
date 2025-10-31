package org.example.loficonnect.dto.request.contact;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactUpdateRequest {
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String phone;
    private String address1;
    private String city;
    private String state;
    private String postalCode;
    private String website;
    private String timezone;
    private Boolean dnd;
    private DndSettings dndSettings;
    private InboundDndSettings inboundDndSettings;
    private List<String> tags;
    private List<CustomField> customFields;
    private String source;
    private String country;
    private String assignedTo;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DndSettings {
        private Channel call;
        private Channel email;
        private Channel sms;
        private Channel whatsApp;
        private Channel gmb;
        private Channel fb;

        @Data
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Channel {
            private String status;
            private String message;
            private String code;
        }
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InboundDndSettings {
        private All all;

        @Data
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class All {
            private String status;
            private String message;
        }
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CustomField {
        private String id;
        private String key;
        private String fieldValue;
    }
}
