package org.example.loficonnect.dto.request.invoicetemplate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceTemplateUpdateRequest {
    private String altId;
    private String altType;
    private Boolean internal;
    private String name;
    private BusinessDetails businessDetails;
    private String currency;
    private List<InvoiceItem> items;
    private Discount discount;
    private String termsNotes;
    private String title;

    @Data
    public static class BusinessDetails {
        private String logoUrl;
        private String name;
        private String phoneNo;
        private String address;
        private String website;
        private List<String> customValues;
    }

    @Data
    public static class InvoiceItem {
        private String name;
        private String description;
        private String productId;
        private String priceId;
        private String currency;
        private Double amount;
        private Integer qty;
        private List<Tax> taxes;
        private String automaticTaxCategoryId;
        private Boolean isSetupFeeItem;
        private String type;
        private Boolean taxInclusive;
    }

    @Data
    public static class Tax {
        private String _id;
        private String name;
        private Double rate;
        private String calculation;
        private String description;
        private String taxId;
    }

    @Data
    public static class Discount {
        private Double value;
        private String type;
        private List<String> validOnProductIds;
    }
}
