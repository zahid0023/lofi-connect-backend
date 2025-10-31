package org.example.loficonnect.dto.mapper.contact;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.contact.ContactUpsertRequest;
import org.example.loficonnect.util.LocationContext;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelContactUpsertRequest {

    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("last_name")
    private String lastName;

    private String name;
    private String email;

    private String locationId;

    private String gender;
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

    @JsonAlias("company_name")
    private String companyName;

    @JsonAlias("assigned_to")
    private String assignedTo;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DndSettings {
        private Channel call;
        private Channel email;
        private Channel sms;
        private Channel whatsapp;
        private Channel gmb;
        private Channel fb;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
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
        private String fieldValue;
    }

    /**
     * Converts ContactUpsertRequest -> GoHighLevelContactUpsertRequest using ObjectMapper.
     */
    public static GoHighLevelContactUpsertRequest fromRequest(ContactUpsertRequest request, ObjectMapper objectMapper) {
        GoHighLevelContactUpsertRequest ghl = objectMapper.convertValue(request, GoHighLevelContactUpsertRequest.class);
        ghl.setLocationId(LocationContext.getLocationId());
        return objectMapper.convertValue(request, GoHighLevelContactUpsertRequest.class);
    }
}