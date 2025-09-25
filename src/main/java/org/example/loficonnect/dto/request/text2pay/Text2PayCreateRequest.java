package org.example.loficonnect.dto.request.text2pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Text2PayCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private String currency;
    private List<Text2PayItem> items;
    private String termsNotes;
    private String title;
    private ContactDetails contactDetails;
    private String invoiceNumber;
    private String issueDate;
    private String dueDate;
    private SentTo sentTo;
    private Boolean liveMode;
    private Boolean automaticTaxesEnabled;
    private PaymentSchedule paymentSchedule;
    private LateFeesConfiguration lateFeesConfiguration;
    private TipsConfiguration tipsConfiguration;
    private String invoiceNumberPrefix;
    private PaymentMethods paymentMethods;
    private List<Attachment> attachments;
    private String id;
    private Boolean includeTermsNote;
    private String action;
    private String userId;
    private Discount discount;
    private BusinessDetails businessDetails;

    @Data
    public static class Text2PayItem {
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
    public static class ContactDetails {
        private String id;
        private String name;
        private String phoneNo;
        private String email;
        private List<AdditionalEmail> additionalEmails;
        private String companyName;
        private Address address;
        private List<String> customFields;
    }

    @Data
    public static class AdditionalEmail {
        private String email;
    }

    @Data
    public static class Address {
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String countryCode;
        private String postalCode;
    }

    @Data
    public static class SentTo {
        private List<String> email;
        private List<String> emailCc;
        private List<String> emailBcc;
        private List<String> phoneNo;
    }

    @Data
    public static class PaymentSchedule {
        private String type;
        private List<String> schedules;
    }

    @Data
    public static class LateFeesConfiguration {
        private Boolean enable;
        private Double value;
        private String type;
        private Frequency frequency;
        private Grace grace;
        private MaxLateFees maxLateFees;
    }

    @Data
    public static class Frequency {
        private Integer intervalCount;
        private String interval;
    }

    @Data
    public static class Grace {
        private Integer intervalCount;
        private String interval;
    }

    @Data
    public static class MaxLateFees {
        private String type;
        private String value;
    }

    @Data
    public static class TipsConfiguration {
        private List<Integer> tipsPercentage;
        private Boolean tipsEnabled;
    }

    @Data
    public static class PaymentMethods {
        private Stripe stripe;
    }

    @Data
    public static class Stripe {
        private Boolean enableBankDebitOnly;
    }

    @Data
    public static class Attachment {
        private String id;
        private String name;
        private String url;
        private String type;
        private Integer size;
    }

    @Data
    public static class Discount {
        private Double value;
        private String type;
        private List<String> validOnProductIds;
    }

    @Data
    public static class BusinessDetails {
        private String logoUrl;
        private String name;
        private String phoneNo;
        private String address;
        private String website;
        private List<String> customValues;
    }
}
