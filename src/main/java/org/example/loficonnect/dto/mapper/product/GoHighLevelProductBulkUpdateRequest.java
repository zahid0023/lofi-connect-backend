package org.example.loficonnect.dto.mapper.product;

import lombok.Data;
import org.example.loficonnect.dto.request.product.ProductBulkUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelProductBulkUpdateRequest {
    private String altId;
    private String altType;
    private String type;
    private List<String> productIds;
    private Filters filters;
    private Price price;
    private Price compareAtPrice;
    private Boolean availability;
    private List<String> collectionIds;
    private String currency;

    @Data
    public static class Filters {
        private List<String> collectionIds;
        private String productType;
        private Boolean availableInStore;
        private String search;
    }

    @Data
    public static class Price {
        private String type;
        private Integer value;
        private Boolean roundToWhole;
    }

    public static GoHighLevelProductBulkUpdateRequest fromRequest(ProductBulkUpdateRequest request) {
        GoHighLevelProductBulkUpdateRequest ghlRequest = new GoHighLevelProductBulkUpdateRequest();

        ghlRequest.setAltId(request.getAlt_id());
        ghlRequest.setAltType(request.getAlt_type());
        ghlRequest.setType(request.getType());
        ghlRequest.setProductIds(request.getProduct_ids());
        ghlRequest.setAvailability(request.getAvailability());
        ghlRequest.setCollectionIds(request.getCollection_ids());
        ghlRequest.setCurrency(request.getCurrency());

        if (request.getFilters() != null) {
            Filters filters = new Filters();
            filters.setCollectionIds(request.getFilters().getCollection_ids());
            filters.setProductType(request.getFilters().getProduct_type());
            filters.setAvailableInStore(request.getFilters().getAvailable_in_store());
            filters.setSearch(request.getFilters().getSearch());
            ghlRequest.setFilters(filters);
        }

        if (request.getPrice() != null) {
            Price price = new Price();
            price.setType(request.getPrice().getType());
            price.setValue(request.getPrice().getValue());
            price.setRoundToWhole(request.getPrice().getRound_to_whole());
            ghlRequest.setPrice(price);
        }

        if (request.getCompare_at_price() != null) {
            Price compareAtPrice = new Price();
            compareAtPrice.setType(request.getCompare_at_price().getType());
            compareAtPrice.setValue(request.getCompare_at_price().getValue());
            compareAtPrice.setRoundToWhole(request.getCompare_at_price().getRound_to_whole());
            ghlRequest.setCompareAtPrice(compareAtPrice);
        }

        return ghlRequest;
    }
}
