package org.example.loficonnect.dto.request.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductUpdateRequest {
    private String name;
    private String location_id;
    private String description;
    private String product_type;
    private String image;
    private String statement_descriptor;
    private Boolean available_in_store;
    private List<Media> medias;
    private List<Variant> variants;
    private List<String> collection_ids;
    private Boolean is_taxes_enabled;
    private List<String> taxes;
    private String automatic_tax_category_id;
    private Boolean is_label_enabled;
    private Label label;
    private String slug;
    private Seo seo;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Media {
        private String id;
        private String title;
        private String url;
        private String type;
        private Boolean is_featured;
        private String price_ids;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Variant {
        private String id;
        private String name;
        private List<Option> options;

        @Data
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Option {
            private String id;
            private String name;
        }
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Label {
        private String title;
        private String start_date;
        private String end_date;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Seo {
        private String title;
        private String description;
    }
}
