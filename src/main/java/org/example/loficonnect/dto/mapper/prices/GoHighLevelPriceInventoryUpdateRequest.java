package org.example.loficonnect.dto.mapper.prices;

import lombok.Data;
import org.example.loficonnect.dto.request.prices.PriceInventoryUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelPriceInventoryUpdateRequest {
    private String altId;
    private String altType;
    private List<Item> items;

    @Data
    public static class Item {
        private String priceId;
        private Integer availableQuantity;
        private Boolean allowOutOfStockPurchases;
    }

    public static GoHighLevelPriceInventoryUpdateRequest fromRequest(PriceInventoryUpdateRequest request) {
        GoHighLevelPriceInventoryUpdateRequest ghl = new GoHighLevelPriceInventoryUpdateRequest();
        ghl.setAltId(request.getAlt_id());
        ghl.setAltType(request.getAlt_type());
        ghl.setItems(request.getItems().stream().map(item -> {
            Item i = new Item();
            i.setPriceId(item.getPrice_id());
            i.setAvailableQuantity(item.getAvailable_quantity());
            i.setAllowOutOfStockPurchases(item.getAllow_out_of_stock_purchases());
            return i;
        }).collect(Collectors.toList()));
        return ghl;
    }
}
