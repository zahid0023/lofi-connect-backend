package org.example.loficonnect.dto.request.shippingzonerate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShippingZoneRateUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private String description;
    private String currency;
    private Double amount;
    private String conditionType;
    private Double minCondition;
    private Double maxCondition;
    private Boolean isCarrierRate;
    private String shippingCarrierId;
    private Double percentageOfRateFee;
    private List<ShippingCarrierServiceRequest> shippingCarrierServices;

    @Data
    public static class ShippingCarrierServiceRequest {
        private String name;
        private String value;
    }
}
