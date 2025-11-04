package org.example.loficonnect.dto.mapper.contact;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.contact.ContactUpdateRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelContactUpdateRequest {
    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("last_name")
    private String lastName;

    private String name;

    private String email;

    private String phone;

    @JsonAlias("address_1")
    private String address1;

    private String city;
    private String state;

    @JsonAlias("postal_code")
    private String postalCode;

    private String website;
    private String timezone;
    private Boolean dnd;

    @JsonAlias("dnd_settings")
    private DndSettings dndSettings;

    @JsonAlias("inbound_dnd_settings")
    private InboundDndSettings inboundDndSettings;

    private List<String> tags;

    @JsonAlias("custom_fields")
    private List<CustomField> customFields;

    private String source;
    private String country;

    @JsonAlias("assigned_to")
    private String assignedTo;

    @Data
    public static class DndSettings {
        @JsonAlias("call")
        private Channel Call;

        @JsonAlias("email")
        private Channel Email;

        @JsonAlias("sms")
        private Channel SMS;

        @JsonAlias("whats_app")
        private Channel WhatsApp;

        @JsonAlias("gmb")
        private Channel GMB;

        @JsonAlias("fb")
        private Channel FB;

        @Data
        public static class Channel {
            private String status;
            private String message;
            private String code;
        }
    }

    @Data
    public static class InboundDndSettings {
        private All all;

        @Data
        public static class All {
            private String status;
            private String message;
        }
    }

    @Data
    public static class CustomField {
        private String id;
        private String key;

        @JsonAlias("field_value")
        private String field_value;
    }

    private GoHighLevelContactUpdateRequest() {
    }

    /**
     * Converts ContactUpdateRequest -> GoHighLevelContactUpdateRequest using ObjectMapper
     */
    public static GoHighLevelContactUpdateRequest fromRequest(ContactUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelContactUpdateRequest.class);
    }
}