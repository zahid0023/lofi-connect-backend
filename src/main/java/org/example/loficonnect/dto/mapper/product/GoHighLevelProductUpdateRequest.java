package org.example.loficonnect.dto.mapper.product;

import lombok.Data;
import org.example.loficonnect.dto.request.product.ProductUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelProductUpdateRequest {
    private String name;
    private String locationId;
    private String description;
    private String productType;
    private String image;
    private String statementDescriptor;
    private Boolean availableInStore;
    private List<Media> medias;
    private List<Variant> variants;
    private List<String> collectionIds;
    private Boolean isTaxesEnabled;
    private List<String> taxes;
    private String automaticTaxCategoryId;
    private Boolean isLabelEnabled;
    private Label label;
    private String slug;
    private Seo seo;

    @Data
    public static class Media {
        private String id;
        private String title;
        private String url;
        private String type;
        private Boolean isFeatured;
        private String priceIds;
    }

    @Data
    public static class Variant {
        private String id;
        private String name;
        private List<Option> options;

        @Data
        public static class Option {
            private String id;
            private String name;
        }
    }

    @Data
    public static class Label {
        private String title;
        private String startDate;
        private String endDate;
    }

    @Data
    public static class Seo {
        private String title;
        private String description;
    }

    public static GoHighLevelProductUpdateRequest fromRequest(ProductUpdateRequest request) {
        GoHighLevelProductUpdateRequest ghl = new GoHighLevelProductUpdateRequest();

        ghl.setName(request.getName());
        ghl.setLocationId(request.getLocation_id());
        ghl.setDescription(request.getDescription());
        ghl.setProductType(request.getProduct_type());
        ghl.setImage(request.getImage());
        ghl.setStatementDescriptor(request.getStatement_descriptor());
        ghl.setAvailableInStore(request.getAvailable_in_store());
        ghl.setCollectionIds(request.getCollection_ids());
        ghl.setIsTaxesEnabled(request.getIs_taxes_enabled());
        ghl.setTaxes(request.getTaxes());
        ghl.setAutomaticTaxCategoryId(request.getAutomatic_tax_category_id());
        ghl.setIsLabelEnabled(request.getIs_label_enabled());
        ghl.setSlug(request.getSlug());

        if (request.getLabel() != null) {
            Label label = new Label();
            label.setTitle(request.getLabel().getTitle());
            label.setStartDate(request.getLabel().getStart_date());
            label.setEndDate(request.getLabel().getEnd_date());
            ghl.setLabel(label);
        }

        if (request.getSeo() != null) {
            Seo seo = new Seo();
            seo.setTitle(request.getSeo().getTitle());
            seo.setDescription(request.getSeo().getDescription());
            ghl.setSeo(seo);
        }

        if (request.getMedias() != null) {
            List<Media> mediaList = request.getMedias().stream().map(m -> {
                Media media = new Media();
                media.setId(m.getId());
                media.setTitle(m.getTitle());
                media.setUrl(m.getUrl());
                media.setType(m.getType());
                media.setIsFeatured(m.getIs_featured());
                media.setPriceIds(m.getPrice_ids());
                return media;
            }).collect(Collectors.toList());
            ghl.setMedias(mediaList);
        }

        if (request.getVariants() != null) {
            List<Variant> variantList = request.getVariants().stream().map(v -> {
                Variant variant = new Variant();
                variant.setId(v.getId());
                variant.setName(v.getName());
                variant.setOptions(v.getOptions().stream().map(o -> {
                    Variant.Option opt = new Variant.Option();
                    opt.setId(o.getId());
                    opt.setName(o.getName());
                    return opt;
                }).collect(Collectors.toList()));
                return variant;
            }).collect(Collectors.toList());
            ghl.setVariants(variantList);
        }

        return ghl;
    }
}
