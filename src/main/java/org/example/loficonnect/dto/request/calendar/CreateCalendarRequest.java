package org.example.loficonnect.dto.request.calendar;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCalendarRequest {
    private boolean isActive;
    private String groupId;
    private List<TeamMember> teamMembers;
    private String eventType;
    private String name;
    private String description;
    private String slug;
    private String widgetSlug;
    private String calendarType;
    private String widgetType;
    private String eventTitle;
    private String eventColor;
    private List<LocationConfiguration> locationConfigurations;
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
    private HashMap<Integer, OpenHour> openHours;
    private Boolean enableRecurring;
    private Recurring recurring;
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
    private List<Availability> availabilities;
    private String guestType;
    private String consentLabel;
    private String calendarCoverImage;
    private LookBusyConfig lookBusyConfig;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TeamMember {
        private String userId;
        private Double priority;
        private Boolean isPrimary;
        private List<LocationConfiguration> locationConfigurations;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LocationConfiguration {
        private String kind;
        private String location;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OpenHour {
        private LocalTime startTime;
        private LocalTime endTime;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Recurring {
        private String freq;
        private Integer count;
        private String bookingOption;
        private String bookingOverlapDefaultStatus;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Availability {
        private LocalDate date;
        private String timeZone;
        private OpenHour openHours;
        private Boolean deleted;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LookBusyConfig {
        private Boolean enabled;
        private Integer lookBusyPercentage;
    }
}