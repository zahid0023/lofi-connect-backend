package org.example.loficonnect.dto.request.schedule;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleCreateRequest {

    private String altId;
    private String altType;
    private String name;
    private ContactDetails contactDetails;
    private Schedule schedule;
    private Boolean liveMode;
    private BusinessDetails businessDetails;
    private String currency;
    private Item[] items;
    private Boolean automaticTaxesEnabled;
    private Discount discount;
    private String termsNotes;
    private String title;
    private TipsConfiguration tipsConfiguration;
    private LateFeesConfiguration lateFeesConfiguration;
    private String invoiceNumberPrefix;
    private PaymentMethods paymentMethods;
    private Attachment[] attachments;

    @Data
    public static class ContactDetails {
        private String id;
        private String name;
        private String phoneNo;
        private String email;
        private AdditionalEmail[] additionalEmails;
        private String companyName;
        private Address address;
        private String[] customFields;
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
    public static class Schedule {
        private String executeAt;
        private Rrule rrule;
    }

    @Data
    public static class Rrule {
        private String intervalType;
        private Integer interval;
        private String startDate;
        private String startTime;
        private String endDate;
        private String endTime;
        private Integer dayOfMonth;
        private String dayOfWeek;
        private Integer numOfWeek;
        private String monthOfYear;
        private Integer count;
        private Integer daysBefore;
        private Boolean useStartAsPrimaryUserAccepted;
        private String endType;
    }

    @Data
    public static class BusinessDetails {
        private String logoUrl;
        private String name;
        private String phoneNo;
        private String address;
        private String website;
        private String[] customValues;
    }

    @Data
    public static class Item {
        private String name;
        private String description;
        private String productId;
        private String priceId;
        private String currency;
        private Integer amount;
        private Integer qty;
        private Tax[] taxes;
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
        private Integer value;
        private String type;
        private String[] validOnProductIds;
    }

    @Data
    public static class TipsConfiguration {
        private Integer[] tipsPercentage;
        private Boolean tipsEnabled;
    }

    @Data
    public static class LateFeesConfiguration {
        private Boolean enable;
        private Integer value;
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
