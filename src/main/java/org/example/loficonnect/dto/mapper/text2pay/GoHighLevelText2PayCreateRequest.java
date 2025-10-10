package org.example.loficonnect.dto.mapper.text2pay;

import lombok.Data;
import org.example.loficonnect.dto.request.text2pay.Text2PayCreateRequest;

import java.util.List;

@Data
public class GoHighLevelText2PayCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private String currency;
    private List<Text2PayCreateRequest.Text2PayItem> items;
    private String termsNotes;
    private String title;
    private Text2PayCreateRequest.ContactDetails contactDetails;
    private String invoiceNumber;
    private String issueDate;
    private String dueDate;
    private Text2PayCreateRequest.SentTo sentTo;
    private Boolean liveMode;
    private Boolean automaticTaxesEnabled;
    private Text2PayCreateRequest.PaymentSchedule paymentSchedule;
    private Text2PayCreateRequest.LateFeesConfiguration lateFeesConfiguration;
    private Text2PayCreateRequest.TipsConfiguration tipsConfiguration;
    private String invoiceNumberPrefix;
    private Text2PayCreateRequest.PaymentMethods paymentMethods;
    private List<Text2PayCreateRequest.Attachment> attachments;
    private String id;
    private Boolean includeTermsNote;
    private String action;
    private String userId;
    private Text2PayCreateRequest.Discount discount;
    private Text2PayCreateRequest.BusinessDetails businessDetails;

    public static GoHighLevelText2PayCreateRequest fromRequest(Text2PayCreateRequest request) {
        GoHighLevelText2PayCreateRequest ghlRequest = new GoHighLevelText2PayCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setCurrency(request.getCurrency());
        ghlRequest.setItems(request.getItems());
        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setContactDetails(request.getContactDetails());
        ghlRequest.setInvoiceNumber(request.getInvoiceNumber());
        ghlRequest.setIssueDate(request.getIssueDate());
        ghlRequest.setDueDate(request.getDueDate());
        ghlRequest.setSentTo(request.getSentTo());
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());
        ghlRequest.setPaymentSchedule(request.getPaymentSchedule());
        ghlRequest.setLateFeesConfiguration(request.getLateFeesConfiguration());
        ghlRequest.setTipsConfiguration(request.getTipsConfiguration());
        ghlRequest.setInvoiceNumberPrefix(request.getInvoiceNumberPrefix());
        ghlRequest.setPaymentMethods(request.getPaymentMethods());
        ghlRequest.setAttachments(request.getAttachments());
        ghlRequest.setId(request.getId());
        ghlRequest.setIncludeTermsNote(request.getIncludeTermsNote());
        ghlRequest.setAction(request.getAction());
        ghlRequest.setUserId(request.getUserId());
        ghlRequest.setDiscount(request.getDiscount());
        ghlRequest.setBusinessDetails(request.getBusinessDetails());

        return ghlRequest;
    }

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
