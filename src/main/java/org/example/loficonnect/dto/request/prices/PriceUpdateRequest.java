package org.example.loficonnect.dto.request.prices;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceUpdateRequest {
    private String name;
    private String type;
    private String currency;
    private Double amount;
    private Recurring recurring;
    private String description;
    private List<MembershipOffer> membership_offers;
    private Integer trial_period;
    private Integer total_cycles;
    private Double setup_fee;
    private List<String> variant_option_ids;
    private Double compare_at_price;
    private String location_id;
    private String user_id;
    private Meta meta;
    private Boolean track_inventory;
    private Integer available_quantity;
    private Boolean allow_out_of_stock_purchases;
    private String sku;
    private ShippingOptions shipping_options;
    private Boolean is_digital_product;
    private List<String> digital_delivery;

    @Data
    public static class Recurring {
        private String interval;
        private Integer interval_count;
    }

    @Data
    public static class MembershipOffer {
        private String label;
        private String value;
        private String _id;
    }

    @Data
    public static class Meta {
        private String source;
        private String source_id;
        private String stripe_price_id;
        private String internal_source;
    }

    @Data
    public static class ShippingOptions {
        private Weight weight;
        private Dimensions dimensions;
    }

    @Data
    public static class Weight {
        private Integer value;
        private String unit;
    }

    @Data
    public static class Dimensions {
        private Integer height;
        private Integer width;
        private Integer length;
        private String unit;
    }
}
