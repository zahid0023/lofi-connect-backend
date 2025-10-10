package org.example.loficonnect.dto.request.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductBulkUpdateRequest {
    private String alt_id;
    private String alt_type;
    private String type;
    private List<String> product_ids;
    private Filters filters;
    private Price price;
    private Price compare_at_price;
    private Boolean availability;
    private List<String> collection_ids;
    private String currency;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Filters {
        private List<String> collection_ids;
        private String product_type;
        private Boolean available_in_store;
        private String search;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Price {
        private String type;
        private Integer value;
        private Boolean round_to_whole;
    }
}
