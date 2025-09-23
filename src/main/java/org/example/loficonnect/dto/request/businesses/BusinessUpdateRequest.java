package org.example.loficonnect.dto.request.businesses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessUpdateRequest {
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
}
