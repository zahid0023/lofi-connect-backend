package org.example.loficonnect.dto.request.shippingzone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShippingZoneUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private List<CountryRequest> countries;

    @Data
    public static class CountryRequest {
        private String code;
        private List<StateRequest> states;
    }

    @Data
    public static class StateRequest {
        private String code;
    }
}
