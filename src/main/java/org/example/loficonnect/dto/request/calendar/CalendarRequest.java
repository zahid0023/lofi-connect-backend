package org.example.loficonnect.dto.request.calendar;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarRequest {
    private Boolean isActive;
    private String locationId;
    private String groupId;
    private List<NotificationRequest> notifications;
    private List<TeamMemberRequest> teamMembers;
    private String eventType;
    private String name;
    private String description;
    private String slug;
    private String widgetSlug;
    private String calendarType;
    private String widgetType;
    private String eventTitle;
    private String eventColor;
    private List<LocationConfigurationRequest> locationConfigurations;
    private Integer slotDuration;
    private String slotDurationUnit;
    private Integer slotInterval;
    private String slotIntervalUnit;
    private Integer slotBuffer;
    private String slotBufferUnit;
    private Integer preBuffer;
    private String preBufferUnit;
    private Integer appointmentPerSlot;
    private Integer appointmentPerDay;
    private Integer allowBookingAfter;
    private String allowBookingAfterUnit;
    private Integer allowBookingFor;
    private String allowBookingForUnit;
    private List<OpenHoursRequest> openHours;
    private Boolean enableRecurring;
    private RecurringRequest recurring;
    private String formId;
    private Boolean stickyContact;
    private Boolean isLivePaymentMode;
    private Boolean autoConfirm;
    private Boolean shouldSendAlertEmailsToAssignedMember;
    private String alertEmail;
    private Boolean googleInvitationEmails;
    private Boolean allowReschedule;
    private Boolean allowCancellation;
    private Boolean shouldAssignContactToTeamMember;
    private Boolean shouldSkipAssigningContactForExisting;
    private String notes;
    private String pixelId;
    private String formSubmitType;
    private String formSubmitRedirectURL;
    private String formSubmitThanksMessage;
    private Integer availabilityType;
    private List<AvailabilityRequest> availabilities;
    private String guestType;
    private String consentLabel;
    private String calendarCoverImage;
    private LookBusyConfigRequest lookBusyConfig;
}
