package org.example.loficonnect.dto.mapper.shippingzonerate;

import lombok.Data;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelShippingZoneRateUpdateRequest {
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
    private List<ShippingCarrierService> shippingCarrierServices;

    @Data
    public static class ShippingCarrierService {
        private String name;
        private String value;
    }

    public static GoHighLevelShippingZoneRateUpdateRequest fromRequest(ShippingZoneRateUpdateRequest request) {
        GoHighLevelShippingZoneRateUpdateRequest ghl = new GoHighLevelShippingZoneRateUpdateRequest();
        ghl.setAltId(request.getAltId());
        ghl.setAltType(request.getAltType());
        ghl.setName(request.getName());
        ghl.setDescription(request.getDescription());
        ghl.setCurrency(request.getCurrency());
        ghl.setAmount(request.getAmount());
        ghl.setConditionType(request.getConditionType());
        ghl.setMinCondition(request.getMinCondition());
        ghl.setMaxCondition(request.getMaxCondition());
        ghl.setIsCarrierRate(request.getIsCarrierRate());
        ghl.setShippingCarrierId(request.getShippingCarrierId());
        ghl.setPercentageOfRateFee(request.getPercentageOfRateFee());

        if (request.getShippingCarrierServices() != null) {
            ghl.setShippingCarrierServices(
                request.getShippingCarrierServices().stream().map(s -> {
                    ShippingCarrierService scs = new ShippingCarrierService();
                    scs.setName(s.getName());
                    scs.setValue(s.getValue());
                    return scs;
                }).collect(Collectors.toList())
            );
        }

        return ghl;
    }
}
