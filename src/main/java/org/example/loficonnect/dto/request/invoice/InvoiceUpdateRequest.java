package org.example.loficonnect.dto.request.invoice;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private String title;
    private String currency;
    private String description;
    private BusinessDetails businessDetails;
    private String invoiceNumber;
    private String contactId;
    private ContactDetails contactDetails;
    private String termsNotes;
    private Discount discount;
    private List<InvoiceItem> invoiceItems;
    private Boolean automaticTaxesEnabled;
    private Boolean liveMode;
    private String issueDate;
    private String dueDate;
    private PaymentSchedule paymentSchedule;
    private TipsConfiguration tipsConfiguration;
    private XeroDetails xeroDetails;
    private String invoiceNumberPrefix;
    private PaymentMethods paymentMethods;
    private List<Attachment> attachments;

    @Data
    public static class BusinessDetails {
        private String name;
        private String address;
        private String phoneNo;
        private String website;
        private String logoUrl;
        private List<String> customValues;
    }

    @Data
    public static class ContactDetails {
        private String id;
        private String name;
        private String phoneNo;
        private String email;
        private List<Email> additionalEmails;
        private String companyName;
        private Address address;
        private List<String> customFields;
    }

    @Data
    public static class Email {
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
    public static class Discount {
        private Integer value;
        private String type;
        private List<String> validOnProductIds;
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
    public static class PaymentSchedule {
        private String type;
        private List<String> schedules;
    }

    @Data
    public static class TipsConfiguration {
        private List<Integer> tipsPercentage;
        private Boolean tipsEnabled;
    }

    @Data
    public static class XeroDetails {}

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
}
