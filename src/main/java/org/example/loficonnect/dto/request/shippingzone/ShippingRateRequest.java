package org.example.loficonnect.dto.request.shippingzone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShippingRateRequest {
    private String altId;
    private String altType;
    private String country;
    private AddressRequest address;
    private String amountAvailable;
    private Double totalOrderAmount;
    private Boolean weightAvailable;
    private Double totalOrderWeight;
    private SourceRequest source;
    private List<ProductRequest> products;
    private String couponCode;

    @Data
    public static class AddressRequest {
        private String name;
        private String companyName;
        private String addressLine1;
        private String country;
        private String state;
        private String city;
        private String zip;
        private String phone;
        private String email;
    }

    @Data
    public static class SourceRequest {
        private String type;
        private String subType;
    }

    @Data
    public static class ProductRequest {
        private String id;
        private Integer qty;
    }
}
