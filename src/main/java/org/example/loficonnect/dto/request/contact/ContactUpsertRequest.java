package org.example.loficonnect.dto.request.contact;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactUpsertRequest {
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String locationId;
    private String gender;
    private String phone;
    private String address1;
    private String city;
    private String state;
    private String postalCode;
    private String website;
    private String timezone;
    private Boolean dnd;
    private Map<String, DndSetting> dndSettings;
    private Map<String, InboundDndSetting> inboundDndSettings;
    private List<String> tags;
    private List<CustomField> customFields;
    private String source;
    private String country;
    private String companyName;
    private String assignedTo;

    @Data
    public static class DndSetting {
        private String status;
        private String message;
        private String code;
    }

    @Data
    public static class InboundDndSetting {
        private String status;
        private String message;
    }

    @Data
    public static class CustomField {
        private String id;
        private String key;
        private Object field_value;
    }
}