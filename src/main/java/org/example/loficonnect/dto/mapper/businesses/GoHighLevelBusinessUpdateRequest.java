package org.example.loficonnect.dto.mapper.businesses;

import lombok.Data;
import org.example.loficonnect.dto.request.businesses.BusinessUpdateRequest;

@Data
public class GoHighLevelBusinessUpdateRequest {
    private String name;
    private String phone;
    private String email;
    private String postalCode;
    private String website;
    private String address;
    private String state;
    private String city;
    private String country;
    private String description;

    public static GoHighLevelBusinessUpdateRequest fromRequest(BusinessUpdateRequest request) {
        GoHighLevelBusinessUpdateRequest ghlRequest = new GoHighLevelBusinessUpdateRequest();

        ghlRequest.setName(request.getName());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setEmail(request.getEmail());
        ghlRequest.setPostalCode(request.getPostalCode());
        ghlRequest.setWebsite(request.getWebsite());
        ghlRequest.setAddress(request.getAddress());
        ghlRequest.setState(request.getState());
        ghlRequest.setCity(request.getCity());
        ghlRequest.setCountry(request.getCountry());
        ghlRequest.setDescription(request.getDescription());

        return ghlRequest;
    }
}
