package org.example.loficonnect.dto.request.coupon;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CouponUpdateRequest {
    private String id;
    private String altId;
    private String altType;
    private String name;
    private String code;
    private String discountType;
    private Double discountValue;
    private String startDate;
    private String endDate;
    private Integer usageLimit;
    private List<String> productIds;
    private Boolean applyToFuturePayments;
    private ApplyToFuturePaymentsConfig applyToFuturePaymentsConfig;
    private Boolean limitPerCustomer;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ApplyToFuturePaymentsConfig {
        private String type;
        private Integer duration;
        private String durationType;
    }
}
