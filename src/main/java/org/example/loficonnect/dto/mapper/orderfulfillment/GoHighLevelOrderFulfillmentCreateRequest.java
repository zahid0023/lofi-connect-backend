package org.example.loficonnect.dto.mapper.orderfulfillment;

import lombok.Data;
import org.example.loficonnect.dto.request.orderfulfillment.OrderFulfillmentCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelOrderFulfillmentCreateRequest {
    private String altId;
    private String altType;
    private List<Tracking> trackings;
    private List<Item> items;
    private Boolean notifyCustomer;

    @Data
    public static class Tracking {
        private String trackingNumber;
        private String shippingCarrier;
        private String trackingUrl;
    }

    @Data
    public static class Item {
        private String priceId;
        private Integer qty;
    }

    public static GoHighLevelOrderFulfillmentCreateRequest fromRequest(OrderFulfillmentCreateRequest request) {
        GoHighLevelOrderFulfillmentCreateRequest ghlRequest = new GoHighLevelOrderFulfillmentCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setNotifyCustomer(request.getNotifyCustomer());

        ghlRequest.setTrackings(request.getTrackings().stream().map(t -> {
            Tracking tracking = new Tracking();
            tracking.setTrackingNumber(t.getTrackingNumber());
            tracking.setShippingCarrier(t.getShippingCarrier());
            tracking.setTrackingUrl(t.getTrackingUrl());
            return tracking;
        }).collect(Collectors.toList()));

        ghlRequest.setItems(request.getItems().stream().map(i -> {
            Item item = new Item();
            item.setPriceId(i.getPriceId());
            item.setQty(i.getQty());
            return item;
        }).collect(Collectors.toList()));

        return ghlRequest;
    }
}
