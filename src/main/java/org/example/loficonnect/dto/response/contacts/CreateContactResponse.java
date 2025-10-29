package org.example.loficonnect.dto.response.contacts;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unexpected fields
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateContactResponse {

    @JsonAlias({"id"})
    private String id;

    @JsonAlias({"dateAdded", "date_added"})
    private String dateAdded;

    @JsonAlias({"dateUpdated", "date_updated"})
    private String dateUpdated;

    @JsonAlias({"deleted"})
    private Boolean deleted;

    @JsonAlias({"tags"})
    private List<String> tags;

    @JsonAlias({"type"})
    private String type;

    @JsonAlias({"customFields", "custom_fields"})
    private List<CustomField> customFields;

    @JsonAlias({"locationId", "location_id"})
    private String locationId;

    @JsonAlias({"firstName", "firsName"})
    private String firstName;

    @JsonAlias({"firstNameLowerCase", "first_name_lower_case"})
    private String firstNameLowerCase;

    @JsonAlias({"fullNameLowerCase", "full_name_lower_case"})
    private String fullNameLowerCase;

    @JsonAlias({"lastName", "lstName"})
    private String lastName;

    @JsonAlias({"lastNameLowerCase", "last_name_lower_case"})
    private String lastNameLowerCase;

    @JsonAlias({"email"})
    private String email;

    @JsonAlias({"emailLowerCase", "email_lower_case"})
    private String emailLowerCase;

    @JsonAlias({"bounceEmail", "bounce_email"})
    private Boolean bounceEmail;

    @JsonAlias({"unsubscribeEmail", "unsubscribe_email"})
    private Boolean unsubscribeEmail;

    @JsonAlias({"dnd"})
    private Boolean dnd;

    @JsonAlias({"dndSettings", "dnd_settings"})
    private Map<String, Object> dndSettings;

    @JsonAlias({"phone"})
    private String phone;

    @JsonAlias({"address1"})
    private String address1;

    @JsonAlias({"city"})
    private String city;

    @JsonAlias({"state"})
    private String state;

    @JsonAlias({"country"})
    private String country;

    @JsonAlias({"postalCode", "postal_code"})
    private String postalCode;

    @JsonAlias({"website"})
    private String website;

    @JsonAlias({"source"})
    private String source;

    @JsonAlias({"companyName", "company_name"})
    private String companyName;

    @JsonAlias({"dateOfBirth", "date_of_birth"})
    private String dateOfBirth;

    @JsonAlias({"birthMonth", "birth_month"})
    private Integer birthMonth;

    @JsonAlias({"birthDay", "birth_day"})
    private Integer birthDay;

    @JsonAlias({"lastSessionActivityAt", "last_session_activity_at"})
    private String lastSessionActivityAt;

    @JsonAlias({"offers"})
    private List<String> offers;

    @JsonAlias({"products"})
    private List<String> products;

    @JsonAlias({"businessId", "business_id"})
    private String businessId;

    @JsonAlias({"assignedTo", "assigned_to"})
    private String assignedTo;

    private CreateContactResponse() {
    }

    // ✅ fromJson with injected ObjectMapper
    public static CreateContactResponse fromJson(JsonNode jsonNode, ObjectMapper mapper) {
        if (jsonNode == null || jsonNode.isNull()) return null;
        return mapper.convertValue(jsonNode, CreateContactResponse.class);
    }

    // ✅ Inner class CustomField
    @Data
    public static class CustomField {
        @JsonAlias({"id"})
        private String id;

        @JsonAlias({"fieldValue"})
        private String value;

        private CustomField() {
        }

        public static CustomField fromJson(JsonNode jsonNode, ObjectMapper mapper) {
            if (jsonNode == null || jsonNode.isNull()) return null;
            return mapper.convertValue(jsonNode, CustomField.class);
        }
    }
}
