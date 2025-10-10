package org.example.loficonnect.dto.request.invoicetemplate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceTemplateCreateRequest {
    private String altId;
    private String altType;
    private Boolean internal;
    private String name;
    private BusinessDetails businessDetails;
    private String currency;
    private List<InvoiceItem> items;
    private Boolean automaticTaxesEnabled;
    private Discount discount;
    private String termsNotes;
    private String title;
    private TipsConfiguration tipsConfiguration;
    private LateFeesConfiguration lateFeesConfiguration;
    private String invoiceNumberPrefix;
    private PaymentMethods paymentMethods;
    private List<String> attachments;

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

    @Data
    public static class TipsConfiguration {
        private List<Integer> tipsPercentage;
        private Boolean tipsEnabled;
    }

    @Data
    public static class LateFeesConfiguration {
        private Boolean enable;
        private Double value;
        private String type;
        private Frequency frequency;
        private Frequency grace;
        private MaxLateFees maxLateFees;
    }

    @Data
    public static class Frequency {
        private Integer intervalCount;
        private String interval;
    }

    @Data
    public static class MaxLateFees {
        private String type;
        private String value;
    }

    @Data
    public static class PaymentMethods {
        private Stripe stripe;
    }

    @Data
    public static class Stripe {
        private Boolean enableBankDebitOnly;
    }
}
