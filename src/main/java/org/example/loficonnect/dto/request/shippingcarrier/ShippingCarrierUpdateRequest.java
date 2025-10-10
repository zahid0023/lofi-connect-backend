package org.example.loficonnect.dto.request.shippingcarrier;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShippingCarrierUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private String callbackUrl;
    private List<Service> services;
    private Boolean allowsMultipleServiceSelection;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Service {
        private String name;
        private String value;
    }
}
