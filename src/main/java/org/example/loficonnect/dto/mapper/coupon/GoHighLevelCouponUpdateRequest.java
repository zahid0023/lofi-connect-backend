package org.example.loficonnect.dto.mapper.coupon;

import lombok.Data;
import org.example.loficonnect.dto.request.coupon.CouponUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelCouponUpdateRequest {
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
    public static class ApplyToFuturePaymentsConfig {
        private String type;
        private Integer duration;
        private String durationType;
    }

    public static GoHighLevelCouponUpdateRequest fromRequest(CouponUpdateRequest request) {
        GoHighLevelCouponUpdateRequest ghl = new GoHighLevelCouponUpdateRequest();

        ghl.setId(request.getId());
        ghl.setAltId(request.getAltId());
        ghl.setAltType(request.getAltType());
        ghl.setName(request.getName());
        ghl.setCode(request.getCode());
        ghl.setDiscountType(request.getDiscountType());
        ghl.setDiscountValue(request.getDiscountValue());
        ghl.setStartDate(request.getStartDate());
        ghl.setEndDate(request.getEndDate());
        ghl.setUsageLimit(request.getUsageLimit());
        ghl.setProductIds(request.getProductIds());
        ghl.setApplyToFuturePayments(request.getApplyToFuturePayments());
        ghl.setLimitPerCustomer(request.getLimitPerCustomer());

        if (request.getApplyToFuturePaymentsConfig() != null) {
            ApplyToFuturePaymentsConfig config = new ApplyToFuturePaymentsConfig();
            config.setType(request.getApplyToFuturePaymentsConfig().getType());
            config.setDuration(request.getApplyToFuturePaymentsConfig().getDuration());
            config.setDurationType(request.getApplyToFuturePaymentsConfig().getDurationType());
            ghl.setApplyToFuturePaymentsConfig(config);
        }

        return ghl;
    }
}
