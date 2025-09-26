package org.example.loficonnect.dto.mapper.schedule;

import lombok.Data;
import org.example.loficonnect.dto.request.schedule.ScheduleCreateRequest;

@Data
public class GoHighLevelScheduleCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private ScheduleCreateRequest.ContactDetails contactDetails;
    private ScheduleCreateRequest.Schedule schedule;
    private Boolean liveMode;
    private ScheduleCreateRequest.BusinessDetails businessDetails;
    private String currency;
    private ScheduleCreateRequest.Item[] items;
    private Boolean automaticTaxesEnabled;
    private ScheduleCreateRequest.Discount discount;
    private String termsNotes;
    private String title;
    private ScheduleCreateRequest.TipsConfiguration tipsConfiguration;
    private ScheduleCreateRequest.LateFeesConfiguration lateFeesConfiguration;
    private String invoiceNumberPrefix;
    private ScheduleCreateRequest.PaymentMethods paymentMethods;
    private ScheduleCreateRequest.Attachment[] attachments;

    public static GoHighLevelScheduleCreateRequest fromRequest(ScheduleCreateRequest request) {
        GoHighLevelScheduleCreateRequest ghlRequest = new GoHighLevelScheduleCreateRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setContactDetails(request.getContactDetails());
        ghlRequest.setSchedule(request.getSchedule());
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setBusinessDetails(request.getBusinessDetails());
        ghlRequest.setCurrency(request.getCurrency());
        ghlRequest.setItems(request.getItems());
        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());
        ghlRequest.setDiscount(request.getDiscount());
        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setTipsConfiguration(request.getTipsConfiguration());
        ghlRequest.setLateFeesConfiguration(request.getLateFeesConfiguration());
        ghlRequest.setInvoiceNumberPrefix(request.getInvoiceNumberPrefix());
        ghlRequest.setPaymentMethods(request.getPaymentMethods());
        ghlRequest.setAttachments(request.getAttachments());

        return ghlRequest;
    }
}
