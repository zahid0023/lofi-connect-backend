package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.InvoiceCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelInvoiceCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private InvoiceCreateRequest.BusinessDetails businessDetails;
    private String currency;
    private List<InvoiceCreateRequest.InvoiceItem> items;
    private InvoiceCreateRequest.Discount discount;
    private String termsNotes;
    private String title;
    private InvoiceCreateRequest.ContactDetails contactDetails;
    private String invoiceNumber;
    private String issueDate;
    private String dueDate;
    private InvoiceCreateRequest.SentTo sentTo;
    private Boolean liveMode;
    private Boolean automaticTaxesEnabled;
    private InvoiceCreateRequest.PaymentSchedule paymentSchedule;
    private InvoiceCreateRequest.LateFeesConfiguration lateFeesConfiguration;
    private InvoiceCreateRequest.TipsConfiguration tipsConfiguration;
    private String invoiceNumberPrefix;
    private InvoiceCreateRequest.PaymentMethods paymentMethods;
    private List<InvoiceCreateRequest.Attachment> attachments;

    // Mapper Method
    public static GoHighLevelInvoiceCreateRequest fromRequest(InvoiceCreateRequest request) {
        GoHighLevelInvoiceCreateRequest ghlRequest = new GoHighLevelInvoiceCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setBusinessDetails(mapBusinessDetails(request.getBusinessDetails()));
        ghlRequest.setCurrency(request.getCurrency());
        ghlRequest.setItems(mapList(request.getItems(), GoHighLevelInvoiceCreateRequest::mapInvoiceItem));
        ghlRequest.setDiscount(request.getDiscount());
        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setContactDetails(mapContactDetails(request.getContactDetails()));
        ghlRequest.setInvoiceNumber(request.getInvoiceNumber());
        ghlRequest.setIssueDate(request.getIssueDate());
        ghlRequest.setDueDate(request.getDueDate());
        ghlRequest.setSentTo(mapSentTo(request.getSentTo()));
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());
        ghlRequest.setPaymentSchedule(mapPaymentSchedule(request.getPaymentSchedule()));
        ghlRequest.setLateFeesConfiguration(mapLateFeesConfiguration(request.getLateFeesConfiguration()));
        ghlRequest.setTipsConfiguration(mapTipsConfiguration(request.getTipsConfiguration()));
        ghlRequest.setInvoiceNumberPrefix(request.getInvoiceNumberPrefix());
        ghlRequest.setPaymentMethods(request.getPaymentMethods());
        ghlRequest.setAttachments(request.getAttachments());

        return ghlRequest;
    }

    // Helper method to map lists generically
    private static <T, R> List<R> mapList(List<T> source, java.util.function.Function<T, R> mapper) {
        return source == null ? null : source.stream().map(mapper).collect(Collectors.toList());
    }

    // Mapping individual objects
    private static InvoiceCreateRequest.BusinessDetails mapBusinessDetails(InvoiceCreateRequest.BusinessDetails details) {
        if (details == null) return null;
        InvoiceCreateRequest.BusinessDetails businessDetails = new InvoiceCreateRequest.BusinessDetails();
        businessDetails.setLogoUrl(details.getLogoUrl());
        businessDetails.setName(details.getName());
        businessDetails.setPhoneNo(details.getPhoneNo());
        businessDetails.setAddress(details.getAddress());
        businessDetails.setWebsite(details.getWebsite());
        businessDetails.setCustomValues(details.getCustomValues());
        return businessDetails;
    }

    private static InvoiceCreateRequest.InvoiceItem mapInvoiceItem(InvoiceCreateRequest.InvoiceItem item) {
        if (item == null) return null;
        InvoiceCreateRequest.InvoiceItem invoiceItem = new InvoiceCreateRequest.InvoiceItem();
        invoiceItem.setName(item.getName());
        invoiceItem.setDescription(item.getDescription());
        invoiceItem.setProductId(item.getProductId());
        invoiceItem.setPriceId(item.getPriceId());
        invoiceItem.setCurrency(item.getCurrency());
        invoiceItem.setAmount(item.getAmount());
        invoiceItem.setQty(item.getQty());
        invoiceItem.setTaxes(mapList(item.getTaxes(), GoHighLevelInvoiceCreateRequest::mapTax));
        invoiceItem.setAutomaticTaxCategoryId(item.getAutomaticTaxCategoryId());
        invoiceItem.setIsSetupFeeItem(item.getIsSetupFeeItem());
        invoiceItem.setType(item.getType());
        invoiceItem.setTaxInclusive(item.getTaxInclusive());
        return invoiceItem;
    }

    private static InvoiceCreateRequest.Tax mapTax(InvoiceCreateRequest.Tax tax) {
        if (tax == null) return null;
        InvoiceCreateRequest.Tax t = new InvoiceCreateRequest.Tax();
        t.set_id(tax.get_id());
        t.setName(tax.getName());
        t.setRate(tax.getRate());
        t.setCalculation(tax.getCalculation());
        t.setDescription(tax.getDescription());
        t.setTaxId(tax.getTaxId());
        return t;
    }

    private static InvoiceCreateRequest.ContactDetails mapContactDetails(InvoiceCreateRequest.ContactDetails details) {
        if (details == null) return null;
        InvoiceCreateRequest.ContactDetails contactDetails = new InvoiceCreateRequest.ContactDetails();
        contactDetails.setId(details.getId());
        contactDetails.setName(details.getName());
        contactDetails.setPhoneNo(details.getPhoneNo());
        contactDetails.setEmail(details.getEmail());
        contactDetails.setAdditionalEmails(details.getAdditionalEmails());
        contactDetails.setCompanyName(details.getCompanyName());
        contactDetails.setAddress(details.getAddress());
        contactDetails.setCustomFields(details.getCustomFields());
        return contactDetails;
    }

    private static InvoiceCreateRequest.SentTo mapSentTo(InvoiceCreateRequest.SentTo sentTo) {
        if (sentTo == null) return null;
        InvoiceCreateRequest.SentTo sentToMapped = new InvoiceCreateRequest.SentTo();
        sentToMapped.setEmail(sentTo.getEmail());
        sentToMapped.setEmailCc(sentTo.getEmailCc());
        sentToMapped.setEmailBcc(sentTo.getEmailBcc());
        sentToMapped.setPhoneNo(sentTo.getPhoneNo());
        return sentToMapped;
    }

    private static InvoiceCreateRequest.PaymentSchedule mapPaymentSchedule(InvoiceCreateRequest.PaymentSchedule schedule) {
        if (schedule == null) return null;
        InvoiceCreateRequest.PaymentSchedule paymentSchedule = new InvoiceCreateRequest.PaymentSchedule();
        paymentSchedule.setType(schedule.getType());
        paymentSchedule.setSchedules(schedule.getSchedules());
        return paymentSchedule;
    }

    private static InvoiceCreateRequest.LateFeesConfiguration mapLateFeesConfiguration(InvoiceCreateRequest.LateFeesConfiguration config) {
        if (config == null) return null;
        InvoiceCreateRequest.LateFeesConfiguration lateFeesConfiguration = new InvoiceCreateRequest.LateFeesConfiguration();
        lateFeesConfiguration.setEnable(config.getEnable());
        lateFeesConfiguration.setValue(config.getValue());
        lateFeesConfiguration.setType(config.getType());
        lateFeesConfiguration.setFrequency(config.getFrequency());
        lateFeesConfiguration.setGrace(config.getGrace());
        lateFeesConfiguration.setMaxLateFees(config.getMaxLateFees());
        return lateFeesConfiguration;
    }

    private static InvoiceCreateRequest.TipsConfiguration mapTipsConfiguration(InvoiceCreateRequest.TipsConfiguration config) {
        if (config == null) return null;
        InvoiceCreateRequest.TipsConfiguration tipsConfiguration = new InvoiceCreateRequest.TipsConfiguration();
        tipsConfiguration.setTipsPercentage(config.getTipsPercentage());
        tipsConfiguration.setTipsEnabled(config.getTipsEnabled());
        return tipsConfiguration;
    }
}
