package org.example.loficonnect.dto.mapper.calendar;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.dto.request.calendar.UpdateCalendarRequest;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelUpdateCalendarRequest {

    @JsonAlias("group_id")
    private String groupId;

    @JsonAlias("team_members")
    private List<TeamMember> teamMembers;

    @JsonAlias("event_type")
    private String eventType;

    private String name;
    private String description;
    private String slug;
    private String widgetSlug;
    private String widgetType;
    private String eventTitle;
    private String eventColor;

    @JsonAlias("location_configurations")
    private List<LocationConfiguration> locationConfigurations;

    @JsonAlias("slot_duration")
    private Integer slotDuration;

    @JsonAlias("slot_duration_unit")
    private String slotDurationUnit;

    @JsonAlias("slot_interval")
    private Integer slotInterval;

    @JsonAlias("slot_interval_unit")
    private String slotIntervalUnit;

    @JsonAlias("slot_buffer")
    private Integer slotBuffer;

    private Integer preBuffer;

    @JsonAlias("appointment_per_slot")
    private Integer appointmentPerSlot;

    @JsonAlias("appointment_per_day")
    private Integer appointmentPerDay;

    @JsonAlias("allow_booking_after")
    private Integer allowBookingAfter;

    @JsonAlias("allow_booking_after_unit")
    private String allowBookingAfterUnit;

    @JsonAlias("allow_booking_for")
    private Integer allowBookingFor;

    @JsonAlias("allow_booking_for_unit")
    private String allowBookingForUnit;

    private List<OpenHours> openHours;
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

    @JsonAlias("look_busy_config")
    private LookBusyConfig lookBusyConfig;

    @JsonAlias("is_active")
    private Boolean isActive;

    public static GoHighLevelUpdateCalendarRequest fromRequest(UpdateCalendarRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelUpdateCalendarRequest.class);
    }

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
    public static class OpenHours {
        private List<Integer> daysOfTheWeek;
        private List<OpenHour> hours;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OpenHour {
        private Integer openHour;
        private Integer openMinute;
        private Integer closeHour;
        private Integer closeMinute;
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
        private String date;
        private String timeZone;
        private List<OpenHour> openHours;
        private Boolean deleted;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LookBusyConfig {
        private Boolean enabled;
        private Integer lookBusyPercentage;
    }
}
