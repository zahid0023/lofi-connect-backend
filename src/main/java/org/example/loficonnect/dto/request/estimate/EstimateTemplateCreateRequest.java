package org.example.loficonnect.dto.request.estimate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EstimateTemplateCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private BusinessDetails businessDetails;
    private String currency;
    private List<Item> items;
    private Boolean liveMode;
    private Discount discount;
    private String termsNotes;
    private String title;
    private Boolean automaticTaxesEnabled;
    private Meta meta;
    private SendEstimateDetails sendEstimateDetails;
    private String estimateNumberPrefix;
    private List<Attachment> attachments;

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
    public static class Item {
        private String name;
        private String description;
        private String productId;
        private String priceId;
        private String currency;
        private Double amount;
        private Integer qty;
    }

    @Data
    public static class Discount {
        private Double value;
        private String type;
        private List<String> validOnProductIds;
    }

    @Data
    public static class SendEstimateDetails {
        private String altId;
        private String altType;
        private String action;
        private Boolean liveMode;
        private String userId;
        private SentFrom sentFrom;
        private String estimateName;
    }

    @Data
    public static class SentFrom {
        private String fromName;
        private String fromEmail;
    }

    @Data
    public static class Meta {
        private String key;
        private String value;
    }

    @Data
    public static class Attachment {
        private String id;
        private String name;
        private String url;
        private String type;
        private Integer size;
    }
}
