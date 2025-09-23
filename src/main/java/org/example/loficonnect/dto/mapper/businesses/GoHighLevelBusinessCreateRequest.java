package org.example.loficonnect.dto.mapper.businesses;

import lombok.Data;
import org.example.loficonnect.dto.request.businesses.BusinessCreateRequest;

@Data
public class GoHighLevelBusinessCreateRequest {
    private String name;
    private String locationId;
    private String phone;
    private String email;
    private String website;
    private String address;
    private String city;
    private String postalCode;
    private String state;
    private String country;
    private String description;

    public static GoHighLevelBusinessCreateRequest fromRequest(BusinessCreateRequest request) {
        GoHighLevelBusinessCreateRequest ghlRequest = new GoHighLevelBusinessCreateRequest();

        ghlRequest.setName(request.getName());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setEmail(request.getEmail());
        ghlRequest.setWebsite(request.getWebsite());
        ghlRequest.setAddress(request.getAddress());
        ghlRequest.setCity(request.getCity());
        ghlRequest.setPostalCode(request.getPostalCode());
        ghlRequest.setState(request.getState());
        ghlRequest.setCountry(request.getCountry());
        ghlRequest.setDescription(request.getDescription());

        return ghlRequest;
    }
}
