package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.InvoiceUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelInvoiceUpdateRequest {
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

    public static GoHighLevelInvoiceUpdateRequest fromRequest(InvoiceUpdateRequest request) {
        GoHighLevelInvoiceUpdateRequest ghlRequest = new GoHighLevelInvoiceUpdateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setCurrency(request.getCurrency());
        ghlRequest.setDescription(request.getDescription());

        if (request.getBusinessDetails() != null) {
            BusinessDetails businessDetails = new BusinessDetails();
            businessDetails.setName(request.getBusinessDetails().getName());
            businessDetails.setAddress(request.getBusinessDetails().getAddress());
            businessDetails.setPhoneNo(request.getBusinessDetails().getPhoneNo());
            businessDetails.setWebsite(request.getBusinessDetails().getWebsite());
            businessDetails.setLogoUrl(request.getBusinessDetails().getLogoUrl());
            businessDetails.setCustomValues(request.getBusinessDetails().getCustomValues());
            ghlRequest.setBusinessDetails(businessDetails);
        }

        ghlRequest.setInvoiceNumber(request.getInvoiceNumber());
        ghlRequest.setContactId(request.getContactId());

        if (request.getContactDetails() != null) {
            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setId(request.getContactDetails().getId());
            contactDetails.setName(request.getContactDetails().getName());
            contactDetails.setPhoneNo(request.getContactDetails().getPhoneNo());
            contactDetails.setEmail(request.getContactDetails().getEmail());

            if (request.getContactDetails().getAdditionalEmails() != null) {
                List<Email> additionalEmails = request.getContactDetails().getAdditionalEmails().stream()
                        .map(e -> {
                            Email email = new Email();
                            email.setEmail(e.getEmail());
                            return email;
                        }).collect(Collectors.toList());
                contactDetails.setAdditionalEmails(additionalEmails);
            }

            contactDetails.setCompanyName(request.getContactDetails().getCompanyName());
            contactDetails.setAddress(mapAddress(request.getContactDetails().getAddress()));
            contactDetails.setCustomFields(request.getContactDetails().getCustomFields());
            ghlRequest.setContactDetails(contactDetails);
        }

        ghlRequest.setTermsNotes(request.getTermsNotes());

        if (request.getDiscount() != null) {
            Discount discount = new Discount();
            discount.setValue(request.getDiscount().getValue());
            discount.setType(request.getDiscount().getType());
            discount.setValidOnProductIds(request.getDiscount().getValidOnProductIds());
            ghlRequest.setDiscount(discount);
        }

        if (request.getInvoiceItems() != null) {
            List<InvoiceItem> invoiceItems = request.getInvoiceItems().stream()
                    .map(item -> {
                        InvoiceItem invoiceItem = new InvoiceItem();
                        invoiceItem.setName(item.getName());
                        invoiceItem.setDescription(item.getDescription());
                        invoiceItem.setProductId(item.getProductId());
                        invoiceItem.setPriceId(item.getPriceId());
                        invoiceItem.setCurrency(item.getCurrency());
                        invoiceItem.setAmount(item.getAmount());
                        invoiceItem.setQty(item.getQty());

                        if (item.getTaxes() != null) {
                            List<Tax> taxes = item.getTaxes().stream()
                                    .map(t -> {
                                        Tax tax = new Tax();
                                        tax.set_id(t.get_id());
                                        tax.setName(t.getName());
                                        tax.setRate(t.getRate());
                                        tax.setCalculation(t.getCalculation());
                                        tax.setDescription(t.getDescription());
                                        tax.setTaxId(t.getTaxId());
                                        return tax;
                                    }).collect(Collectors.toList());
                            invoiceItem.setTaxes(taxes);
                        }
                        invoiceItem.setAutomaticTaxCategoryId(item.getAutomaticTaxCategoryId());
                        invoiceItem.setIsSetupFeeItem(item.getIsSetupFeeItem());
                        invoiceItem.setType(item.getType());
                        invoiceItem.setTaxInclusive(item.getTaxInclusive());
                        return invoiceItem;
                    }).collect(Collectors.toList());
            ghlRequest.setInvoiceItems(invoiceItems);
        }

        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setIssueDate(request.getIssueDate());
        ghlRequest.setDueDate(request.getDueDate());

        if (request.getPaymentSchedule() != null) {
            PaymentSchedule paymentSchedule = new PaymentSchedule();
            paymentSchedule.setType(request.getPaymentSchedule().getType());
            paymentSchedule.setSchedules(request.getPaymentSchedule().getSchedules());
            ghlRequest.setPaymentSchedule(paymentSchedule);
        }

        if (request.getTipsConfiguration() != null) {
            TipsConfiguration tipsConfiguration = new TipsConfiguration();
            tipsConfiguration.setTipsPercentage(request.getTipsConfiguration().getTipsPercentage());
            tipsConfiguration.setTipsEnabled(request.getTipsConfiguration().getTipsEnabled());
            ghlRequest.setTipsConfiguration(tipsConfiguration);
        }

        ghlRequest.setXeroDetails(mapXeroDetails(request.getXeroDetails()));
        ghlRequest.setInvoiceNumberPrefix(request.getInvoiceNumberPrefix());
        ghlRequest.setPaymentMethods(mapPaymentMethods(request.getPaymentMethods()));
        ghlRequest.setAttachments(mapAttachments(request.getAttachments()));

        return ghlRequest;
    }

    private static Address mapAddress(org.example.loficonnect.dto.request.invoice.InvoiceUpdateRequest.Address address) {
        Address addressObj = new Address();
        addressObj.setAddressLine1(address.getAddressLine1());
        addressObj.setAddressLine2(address.getAddressLine2());
        addressObj.setCity(address.getCity());
        addressObj.setState(address.getState());
        addressObj.setCountryCode(address.getCountryCode());
        addressObj.setPostalCode(address.getPostalCode());
        return addressObj;
    }

    private static XeroDetails mapXeroDetails(org.example.loficonnect.dto.request.invoice.InvoiceUpdateRequest.XeroDetails xeroDetails) {
        return new XeroDetails();
    }

    private static PaymentMethods mapPaymentMethods(org.example.loficonnect.dto.request.invoice.InvoiceUpdateRequest.PaymentMethods paymentMethods) {
        PaymentMethods mappedMethods = new PaymentMethods();
        if (paymentMethods != null && paymentMethods.getStripe() != null) {
            Stripe stripe = new Stripe();
            stripe.setEnableBankDebitOnly(paymentMethods.getStripe().getEnableBankDebitOnly());
            mappedMethods.setStripe(stripe);
        }
        return mappedMethods;
    }

    private static List<Attachment> mapAttachments(List<org.example.loficonnect.dto.request.invoice.InvoiceUpdateRequest.Attachment> attachments) {
        return attachments.stream()
                .map(a -> {
                    Attachment attachment = new Attachment();
                    attachment.setId(a.getId());
                    attachment.setName(a.getName());
                    attachment.setUrl(a.getUrl());
                    attachment.setType(a.getType());
                    attachment.setSize(a.getSize());
                    return attachment;
                })
                .collect(Collectors.toList());
    }

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
