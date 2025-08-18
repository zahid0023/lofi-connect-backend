package org.example.loficonnect.dto.request.contact;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactCreateRequest {
    private String first_name;
    private String last_name;
    private String name;
    private String email;
    private String location_id;
    private String gender;
    private String phone;
    private String address1;
    private String city;
    private String state;
    private String postal_code;
    private String website;
    private String timezone;
    private Boolean dnd;
    private DndSettings dnd_settings;
    private InboundDndSettings inbound_dnd_settings;
    private List<String> tags;
    private List<CustomField> custom_fields;
    private String source;
    private String country;
    private String company_name;
    private String assigned_to;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DndSettings {
        private Channel call;
        private Channel email;
        private Channel sms;
        private Channel whats_app;
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
        private String field_value;
    }
}
