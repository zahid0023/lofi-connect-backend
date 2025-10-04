package org.example.loficonnect.dto.request.prices;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceInventoryUpdateRequest {
    private String alt_id;
    private String alt_type;
    private List<Item> items;

    @Data
    public static class Item {
        private String price_id;
        private Integer available_quantity;
        private Boolean allow_out_of_stock_purchases;
    }
}
