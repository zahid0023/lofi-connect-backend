package org.example.loficonnect.dto.mapper.schedule;

import lombok.Data;
import org.example.loficonnect.dto.request.schedule.ScheduleUpdateAndScheduleRequest;

@Data
public class GoHighLevelScheduleUpdateAndScheduleRequest {
    private String altId;
    private String altType;
    private String name;
    private ScheduleUpdateAndScheduleRequest.ContactDetails contactDetails;
    private ScheduleUpdateAndScheduleRequest.Schedule schedule;
    private Boolean liveMode;
    private ScheduleUpdateAndScheduleRequest.BusinessDetails businessDetails;
    private String currency;
    private ScheduleUpdateAndScheduleRequest.Item[] items;
    private ScheduleUpdateAndScheduleRequest.Discount discount;
    private String termsNotes;
    private String title;
    private ScheduleUpdateAndScheduleRequest.Attachment[] attachments;

    public static GoHighLevelScheduleUpdateAndScheduleRequest fromRequest(ScheduleUpdateAndScheduleRequest request) {
        GoHighLevelScheduleUpdateAndScheduleRequest ghlRequest = new GoHighLevelScheduleUpdateAndScheduleRequest();
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
