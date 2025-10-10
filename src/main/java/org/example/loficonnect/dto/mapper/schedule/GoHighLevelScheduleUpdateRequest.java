package org.example.loficonnect.dto.mapper.schedule;

import lombok.Data;
import org.example.loficonnect.dto.request.schedule.ScheduleUpdateRequest;

@Data
public class GoHighLevelScheduleUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private ScheduleUpdateRequest.ContactDetails contactDetails;
    private ScheduleUpdateRequest.Schedule schedule;
    private Boolean liveMode;
    private ScheduleUpdateRequest.BusinessDetails businessDetails;
    private String currency;
    private ScheduleUpdateRequest.Item[] items;
    private ScheduleUpdateRequest.Discount discount;
    private String termsNotes;
    private String title;
    private ScheduleUpdateRequest.Attachment[] attachments;

    public static GoHighLevelScheduleUpdateRequest fromRequest(ScheduleUpdateRequest request) {
        GoHighLevelScheduleUpdateRequest ghlRequest = new GoHighLevelScheduleUpdateRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setContactDetails(request.getContactDetails());
        ghlRequest.setSchedule(request.getSchedule());
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setBusinessDetails(request.getBusinessDetails());
        ghlRequest.setCurrency(request.getCurrency());
        ghlRequest.setItems(request.getItems());
        ghlRequest.setDiscount(request.getDiscount());
        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setAttachments(request.getAttachments());

        return ghlRequest;
    }
}
