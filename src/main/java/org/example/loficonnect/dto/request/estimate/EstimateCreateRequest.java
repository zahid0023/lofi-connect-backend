package org.example.loficonnect.dto.request.estimate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EstimateCreateRequest {
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
    private ContactDetails contactDetails;
    private Integer estimateNumber;
    private String issueDate;
    private String expiryDate;
    private SentTo sentTo;
    private Boolean automaticTaxesEnabled;
    private Meta meta;
    private SendEstimateDetails sendEstimateDetails;
    private FrequencySettings frequencySettings;
    private String estimateNumberPrefix;
    private String userId;
    private List<Attachment> attachments;
    private AutoInvoice autoInvoice;

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
    public static class ContactDetails {
        private String id;
        private String name;
        private String phoneNo;
        private String email;
        private List<String> additionalEmails;
        private String companyName;
        private Address address;
        private List<String> customFields;
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
    public static class Meta {
        private String key;
        private String value;
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
    public static class FrequencySettings {
        private Boolean enabled;
        private Schedule schedule;

        @Data
        public static class Schedule {
            private String executeAt;
            private RRule rrule;
        }

        @Data
        public static class RRule {
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
    public static class AutoInvoice {
        private Boolean enabled;
        private Boolean directPayments;
    }
}
