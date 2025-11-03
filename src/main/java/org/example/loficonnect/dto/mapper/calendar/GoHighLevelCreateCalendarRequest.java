package org.example.loficonnect.dto.mapper.calendar;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.calendar.CreateCalendarRequest;
import org.example.loficonnect.util.LocationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelCreateCalendarRequest {
    @JsonAlias("is_active")
    private Boolean isActive;

    private String locationId;

    @JsonAlias("group_id")
    private String groupId;

    @JsonAlias("team_members")
    private List<TeamMember> teamMembers;

    @JsonAlias("event_type")
    private String eventType;

    private String name;
    private String description;
    private String slug;

    @JsonAlias("widget_slug")
    private String widgetSlug;

    @JsonAlias("calendar_type")
    private String calendarType;

    @JsonAlias("widget_type")
    private String widgetType;

    @JsonAlias("event_title")
    private String eventTitle;

    @JsonAlias("event_color")
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

    @JsonAlias("slot_buffer_unit")
    private String slotBufferUnit;

    @JsonAlias("pre_buffer")
    private Integer preBuffer;

    @JsonAlias("pre_buffer_unit")
    private String preBufferUnit;

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

    @JsonAlias("enable_recurring")
    private Boolean enableRecurring;

    private Recurring recurring;

    @JsonAlias("form_id")
    private String formId;

    @JsonAlias("sticky_contact")
    private Boolean stickyContact;

    @JsonAlias("is_live_payment_mode")
    private Boolean isLivePaymentMode;

    @JsonAlias("auto_confirm")
    private Boolean autoConfirm;

    @JsonAlias("should_send_alert_emails_to_assigned_member")
    private Boolean shouldSendAlertEmailsToAssignedMember;

    @JsonAlias("alert_email")
    private String alertEmail;

    @JsonAlias("google_invitation_emails")
    private Boolean googleInvitationEmails;

    @JsonAlias("allow_reschedule")
    private Boolean allowReschedule;

    @JsonAlias("allow_cancellation")
    private Boolean allowCancellation;

    @JsonAlias("should_assign_contact_to_team_member")
    private Boolean shouldAssignContactToTeamMember;

    @JsonAlias("should_skip_assigning_contact_for_existing")
    private Boolean shouldSkipAssigningContactForExisting;

    private String notes;

    @JsonAlias("pixel_id")
    private String pixelId;

    @JsonAlias("form_submit_type")
    private String formSubmitType;

    @JsonAlias("form_submit_redirect_url")
    private String formSubmitRedirectURL;

    @JsonAlias("form_submit_thanks_message")
    private String formSubmitThanksMessage;

    @JsonAlias("availability_type")
    private Integer availabilityType;

    private List<Availability> availabilities;

    @JsonAlias("guest_type")
    private String guestType;

    @JsonAlias("consent_label")
    private String consentLabel;

    @JsonAlias("calendar_cover_image")
    private String calendarCoverImage;

    @JsonAlias("look_busy_config")
    private LookBusyConfig lookBusyConfig;

    // Nested classes
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamMember {
        @JsonAlias("user_id")
        private String userId;
        private Double priority;

        @JsonAlias("is_primary")
        private Boolean isPrimary;

        @JsonAlias("location_configurations")
        private List<LocationConfiguration> locationConfigurations;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationConfiguration {
        private String kind;
        private String location;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recurring {
        private String freq;
        private Integer count;
        @JsonAlias("booking_option")
        private String bookingOption;
        @JsonAlias("booking_overlap_default_status")
        private String bookingOverlapDefaultStatus;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Availability {
        private String date; // GoHighLevel expects ISO date string
        @JsonAlias("time_zone")
        private String timeZone;
        @JsonAlias("open_hours")
        private OpenHour openHours;
        private Boolean deleted;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LookBusyConfig {
        private Boolean enabled;
        @JsonAlias("look_busy_percentage")
        private Integer lookBusyPercentage;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenHours {
        List<Integer> daysOfTheWeek;
        List<OpenHour> hours;

        private OpenHours() {
        }

        public static List<OpenHours> fromOpenHours(HashMap<Integer, CreateCalendarRequest.OpenHour> openHours) {
            List<OpenHours> openHoursList = new ArrayList<>();
            openHours.forEach((k, v) -> {
                OpenHours hours = new OpenHours();
                hours.setDaysOfTheWeek(List.of(k));
                hours.setHours(List.of(OpenHour.fromOpenHour(v)));
                openHoursList.add(hours);
            });
            return openHoursList;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenHour {
        Integer openHour;
        Integer OpenMinute;
        Integer closeHour;
        Integer closeMinute;

        private OpenHour() {
        }

        public static OpenHour fromOpenHour(CreateCalendarRequest.OpenHour request) {
            OpenHour openHour = new OpenHour();
            openHour.setOpenHour(request.getStartTime().getHour());
            openHour.setOpenMinute(request.getStartTime().getMinute());
            openHour.setCloseHour(request.getEndTime().getHour());
            openHour.setCloseMinute(request.getEndTime().getMinute());
            return openHour;
        }
    }

    private GoHighLevelCreateCalendarRequest() {
    }


    /**
     * Converts CreateCalendarRequest -> GoHighLevelCreateCalendarRequest using ObjectMapper
     */
    public static GoHighLevelCreateCalendarRequest fromRequest(CreateCalendarRequest request, ObjectMapper objectMapper) {
        GoHighLevelCreateCalendarRequest ghl = objectMapper.convertValue(request, GoHighLevelCreateCalendarRequest.class);
        ghl.setLocationId(LocationContext.getLocationId());
        ghl.setOpenHours(OpenHours.fromOpenHours(request.getOpenHours()));
        return ghl;
    }
}
