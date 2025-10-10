package org.example.loficonnect.dto.request.orderfulfillment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderFulfillmentCreateRequest {
    private String altId;
    private String altType;
    private List<Tracking> trackings;
    private List<Item> items;
    private Boolean notifyCustomer;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Tracking {
        private String trackingNumber;
        private String shippingCarrier;
        private String trackingUrl;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Item {
        private String priceId;
        private Integer qty;
    }
}
