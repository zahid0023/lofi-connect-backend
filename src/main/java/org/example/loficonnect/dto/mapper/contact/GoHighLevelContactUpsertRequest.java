package org.example.loficonnect.dto.mapper.contact;

import lombok.Data;
import org.example.loficonnect.dto.request.contact.ContactUpsertRequest;

import java.util.List;
import java.util.Map;

@Data
public class GoHighLevelContactUpsertRequest {
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
    private Map<String, ContactUpsertRequest.DndSetting> dndSettings;
    private Map<String, ContactUpsertRequest.InboundDndSetting> inboundDndSettings;
    private List<String> tags;
    private List<ContactUpsertRequest.CustomField> customFields;
    private String source;
    private String country;
    private String companyName;
    private String assignedTo;

    public static GoHighLevelContactUpsertRequest fromRequest(ContactUpsertRequest request) {
        GoHighLevelContactUpsertRequest ghl = new GoHighLevelContactUpsertRequest();
        ghl.setFirstName(request.getFirstName());
        ghl.setLastName(request.getLastName());
        ghl.setName(request.getName());
        ghl.setEmail(request.getEmail());
        ghl.setLocationId(request.getLocationId());
        ghl.setGender(request.getGender());
        ghl.setPhone(request.getPhone());
        ghl.setAddress1(request.getAddress1());
        ghl.setCity(request.getCity());
        ghl.setState(request.getState());
        ghl.setPostalCode(request.getPostalCode());
        ghl.setWebsite(request.getWebsite());
        ghl.setTimezone(request.getTimezone());
        ghl.setDnd(request.getDnd());
        ghl.setDndSettings(request.getDndSettings());
        ghl.setInboundDndSettings(request.getInboundDndSettings());
        ghl.setTags(request.getTags());
        ghl.setCustomFields(request.getCustomFields());
        ghl.setSource(request.getSource());
        ghl.setCountry(request.getCountry());
        ghl.setCompanyName(request.getCompanyName());
        ghl.setAssignedTo(request.getAssignedTo());
        return ghl;
    }
}
