package org.example.loficonnect.dto.mapper.contact;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.contact.ContactCreateRequest;
import org.example.loficonnect.util.LocationContext;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelContactCreateRequest {
    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("last_name")
    private String lastName;

    private String name;
    private String email;

    @JsonAlias("location_id")
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
        private Channel Email;
        private Channel SMS;
        private Channel WhatsApp;
        private Channel GMB;
        private Channel FB;

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
        private String field_value;
    }

    public static GoHighLevelContactCreateRequest fromRequest(ContactCreateRequest request, ObjectMapper objectMapper) {
        GoHighLevelContactCreateRequest ghlRequest = new GoHighLevelContactCreateRequest();
        ghlRequest.setFirstName(request.getFirstName());
        ghlRequest.setLastName(request.getLastName());
        ghlRequest.setName(request.getName());
        ghlRequest.setEmail(request.getEmail());
        ghlRequest.setLocationId(LocationContext.getLocationId());
        ghlRequest.setGender(request.getGender());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setAddress1(request.getAddress1());
        ghlRequest.setCity(request.getCity());
        ghlRequest.setState(request.getState());
        ghlRequest.setPostalCode(request.getPostalCode());
        ghlRequest.setWebsite(request.getWebsite());
        ghlRequest.setTimezone(request.getTimezone());
        ghlRequest.setDnd(request.getDnd());
        ghlRequest.setDndSettings(request.getDndSettings() != null ? mapDndSettings(request.getDndSettings()) : null);
        ghlRequest.setInboundDndSettings(request.getInboundDndSettings() != null ? mapInboundDndSettings(request.getInboundDndSettings()) : null);
        ghlRequest.setTags(request.getTags());
        ghlRequest.setCustomFields(request.getCustomFields() != null ? mapCustomFields(request.getCustomFields()) : null);
        ghlRequest.setSource(request.getSource());
        ghlRequest.setCountry(request.getCountry());
        ghlRequest.setCompanyName(request.getCompanyName());
        ghlRequest.setAssignedTo(request.getAssignedTo());
        return objectMapper.convertValue(ghlRequest, GoHighLevelContactCreateRequest.class);
    }

    private static DndSettings mapDndSettings(ContactCreateRequest.DndSettings req) {
        DndSettings dndSettings = new DndSettings();
        if (req.getCall() != null) dndSettings.setCall(mapChannel(req.getCall()));
        if (req.getEmail() != null) dndSettings.setEmail(mapChannel(req.getEmail()));
        if (req.getSms() != null) dndSettings.setSMS(mapChannel(req.getSms()));
        if (req.getWhatsApp() != null) dndSettings.setWhatsApp(mapChannel(req.getWhatsApp()));
        if (req.getGmb() != null) dndSettings.setGMB(mapChannel(req.getGmb()));
        if (req.getFb() != null) dndSettings.setFB(mapChannel(req.getFb()));
        return dndSettings;
    }

    private static DndSettings.Channel mapChannel(ContactCreateRequest.DndSettings.Channel req) {
        DndSettings.Channel channel = new DndSettings.Channel();
        channel.setStatus(req.getStatus());
        channel.setMessage(req.getMessage());
        channel.setCode(req.getCode());
        return channel;
    }

    private static InboundDndSettings mapInboundDndSettings(ContactCreateRequest.InboundDndSettings req) {
        InboundDndSettings inbound = new InboundDndSettings();
        if (req.getAll() != null) {
            InboundDndSettings.All all = new InboundDndSettings.All();
            all.setStatus(req.getAll().getStatus());
            all.setMessage(req.getAll().getMessage());
            inbound.setAll(all);
        }
        return inbound;
    }

    private static List<CustomField> mapCustomFields(List<ContactCreateRequest.CustomField> reqList) {
        return reqList.stream().map(req -> {
            CustomField cf = new CustomField();
            cf.setId(req.getId());
            cf.setKey(req.getKey());
            cf.setField_value(req.getFieldValue());
            return cf;
        }).toList();
    }
}
