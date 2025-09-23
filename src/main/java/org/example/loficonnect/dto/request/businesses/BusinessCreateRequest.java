package org.example.loficonnect.dto.request.businesses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessCreateRequest {
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
}
