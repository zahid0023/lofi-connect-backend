package org.example.loficonnect.dto.mapper.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.loficonnect.dto.request.contact.ContactUpdateRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class GoHighLevelContactUpdateRequest {
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
    public static class DndSettings {
        private Channel Call;
        private Channel Email;
        private Channel SMS;
        private Channel WhatsApp;
        private Channel GMB;
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
        private String field_value;
    }

    public static GoHighLevelContactUpdateRequest fromRequest(ContactUpdateRequest request) {
        GoHighLevelContactUpdateRequest ghlRequest = new GoHighLevelContactUpdateRequest();
        ghlRequest.setFirstName(request.getFirst_name());
        ghlRequest.setLastName(request.getLast_name());
        ghlRequest.setName(request.getName());
        ghlRequest.setEmail(request.getEmail());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setAddress1(request.getAddress1());
        ghlRequest.setCity(request.getCity());
        ghlRequest.setState(request.getState());
        ghlRequest.setPostalCode(request.getPostal_code());
        ghlRequest.setWebsite(request.getWebsite());
        ghlRequest.setTimezone(request.getTimezone());
        ghlRequest.setDnd(request.getDnd());
        ghlRequest.setDndSettings(request.getDnd_settings() != null ? mapDndSettings(request.getDnd_settings()) : null);
        ghlRequest.setInboundDndSettings(request.getInbound_dnd_settings() != null ? mapInboundDndSettings(request.getInbound_dnd_settings()) : null);
        ghlRequest.setTags(request.getTags());
        ghlRequest.setCustomFields(request.getCustom_fields() != null ? mapCustomFields(request.getCustom_fields()) : null);
        ghlRequest.setSource(request.getSource());
        ghlRequest.setCountry(request.getCountry());
        ghlRequest.setAssignedTo(request.getAssigned_to());
        return ghlRequest;
    }

    private static DndSettings mapDndSettings(ContactUpdateRequest.DndSettings req) {
        DndSettings dndSettings = new DndSettings();
        if (req.getCall() != null) dndSettings.setCall(mapChannel(req.getCall()));
        if (req.getEmail() != null) dndSettings.setEmail(mapChannel(req.getEmail()));
        if (req.getSms() != null) dndSettings.setSMS(mapChannel(req.getSms()));
        if (req.getWhats_app() != null) dndSettings.setWhatsApp(mapChannel(req.getWhats_app()));
        if (req.getGmb() != null) dndSettings.setGMB(mapChannel(req.getGmb()));
        if (req.getFb() != null) dndSettings.setFB(mapChannel(req.getFb()));
        return dndSettings;
    }

    private static DndSettings.Channel mapChannel(ContactUpdateRequest.DndSettings.Channel req) {
        DndSettings.Channel channel = new DndSettings.Channel();
        channel.setStatus(req.getStatus());
        channel.setMessage(req.getMessage());
        channel.setCode(req.getCode());
        return channel;
    }

    private static InboundDndSettings mapInboundDndSettings(ContactUpdateRequest.InboundDndSettings req) {
        InboundDndSettings inbound = new InboundDndSettings();
        if (req.getAll() != null) {
            InboundDndSettings.All all = new InboundDndSettings.All();
            all.setStatus(req.getAll().getStatus());
            all.setMessage(req.getAll().getMessage());
            inbound.setAll(all);
        }
        return inbound;
    }

    private static List<CustomField> mapCustomFields(List<ContactUpdateRequest.CustomField> reqList) {
        return reqList.stream().map(req -> {
            CustomField cf = new CustomField();
            cf.setId(req.getId());
            cf.setKey(req.getKey());
            cf.setField_value(req.getField_value());
            return cf;
        }).toList();
    }
}
