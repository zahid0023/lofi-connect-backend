package org.example.loficonnect.dto.request.storesetting;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreSettingCreateRequest {
    private String altId;
    private String altType;
    private ShippingOrigin shippingOrigin;
    private StoreOrderNotification storeOrderNotification;
    private StoreOrderFulfillmentNotification storeOrderFulfillmentNotification;

    @Data
    public static class ShippingOrigin {
        private String name;
        private String country;
        private String state;
        private String city;
        private String street1;
        private String street2;
        private String zip;
        private String phone;
        private String email;
    }

    @Data
    public static class StoreOrderNotification {
        private Boolean enabled;
        private String subject;
        private String emailTemplateId;
        private String defaultEmailTemplateId;
    }

    @Data
    public static class StoreOrderFulfillmentNotification {
        private Boolean enabled;
        private String subject;
        private String emailTemplateId;
        private String defaultEmailTemplateId;
    }
}
