package org.example.loficonnect.dto.mapper.calendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.loficonnect.dto.request.calendar.CalendarRequest;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GoHighLevelCalendarRequest {

    protected Boolean isActive;
    protected String groupId;
    protected List<TeamMember> teamMembers;
    protected String eventType;
    protected String name;
    protected String description;
    protected String slug;
    protected String widgetSlug;
    protected String widgetType;
    protected String eventTitle;
    protected String eventColor;
    protected List<LocationConfiguration> locationConfigurations;
    protected Integer slotDuration;
    protected String slotDurationUnit;
    protected Integer slotInterval;
    protected String slotIntervalUnit;
    protected Integer slotBuffer;
    protected String slotBufferUnit;
    protected Integer preBuffer;
    protected String preBufferUnit;
    protected Integer appointmentPerSlot;
    protected Integer appointmentPerDay;
    protected Integer allowBookingAfter;
    protected String allowBookingAfterUnit;
    protected Integer allowBookingFor;
    protected String allowBookingForUnit;
    protected List<OpenHours> openHours;
    protected Boolean enableRecurring;
    protected Recurring recurring;
    protected String formId;
    protected Boolean stickyContact;
    protected Boolean isLivePaymentMode;
    protected Boolean autoConfirm;
    protected Boolean shouldSendAlertEmailsToAssignedMember;
    protected String alertEmail;
    protected Boolean googleInvitationEmails;
    protected Boolean allowReschedule;
    protected Boolean allowCancellation;
    protected Boolean shouldAssignContactToTeamMember;
    protected Boolean shouldSkipAssigningContactForExisting;
    protected String notes;
    protected String pixelId;
    protected String formSubmitType;
    protected String formSubmitRedirectURL;
    protected String formSubmitThanksMessage;
    protected Integer availabilityType;
    protected List<Availability> availabilities;
    protected String guestType;
    protected String consentLabel;
    protected String calendarCoverImage;
    protected LookBusyConfig lookBusyConfig;

    public static GoHighLevelCalendarRequest fromRequest(final CalendarRequest request) {
        if (request == null) return null;

        GoHighLevelCalendarRequest ghlRequest = new GoHighLevelCalendarRequest();

        ghlRequest.setGroupId(request.getGroupId());

        ghlRequest.setTeamMembers(request.getTeamMembers() != null
                ? request.getTeamMembers().stream()
                .map(GoHighLevelCalendarRequest.TeamMember::fromTeamMemberRequest)
                .collect(Collectors.toList())
                : null);

        ghlRequest.setEventType(request.getEventType());
        ghlRequest.setName(request.getName());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setSlug(request.getSlug());
        ghlRequest.setWidgetSlug(request.getWidgetSlug());
        ghlRequest.setWidgetType(request.getWidgetType());
        ghlRequest.setEventTitle(request.getEventTitle());
        ghlRequest.setEventColor(request.getEventColor());

        ghlRequest.setLocationConfigurations(request.getLocationConfigurations() != null
                ? request.getLocationConfigurations().stream()
                .map(GoHighLevelCalendarRequest.LocationConfiguration::fromLocationConfigurationRequest)
                .collect(Collectors.toList())
                : null);

        ghlRequest.setSlotDuration(request.getSlotDuration());
        ghlRequest.setSlotDurationUnit(request.getSlotDurationUnit());
        ghlRequest.setSlotInterval(request.getSlotInterval());
        ghlRequest.setSlotIntervalUnit(request.getSlotIntervalUnit());
        ghlRequest.setSlotBuffer(request.getSlotBuffer());
        ghlRequest.setPreBuffer(request.getPreBuffer());
        ghlRequest.setPreBufferUnit(request.getPreBufferUnit());
        ghlRequest.setAppointmentPerSlot(request.getAppointmentPerSlot());
        ghlRequest.setAppointmentPerDay(request.getAppointmentPerDay());
        ghlRequest.setAllowBookingAfter(request.getAllowBookingAfter());
        ghlRequest.setAllowBookingAfterUnit(request.getAllowBookingAfterUnit());
        ghlRequest.setAllowBookingFor(request.getAllowBookingFor());
        ghlRequest.setAllowBookingForUnit(request.getAllowBookingForUnit());

        ghlRequest.setOpenHours(request.getOpenHours() != null
                ? request.getOpenHours().stream()
                .map(GoHighLevelCalendarUpdateRequest.OpenHours::fromOpenHoursRequest)
                .collect(Collectors.toList())
                : null);

        ghlRequest.setEnableRecurring(request.getEnableRecurring());
        ghlRequest.setRecurring(request.getRecurring() != null
                ? GoHighLevelCalendarRequest.Recurring.fromRecurringRequest(request.getRecurring())
                : null);

        ghlRequest.setFormId(request.getFormId());
        ghlRequest.setStickyContact(request.getStickyContact());
        ghlRequest.setIsLivePaymentMode(request.getIsLivePaymentMode());
        ghlRequest.setAutoConfirm(request.getAutoConfirm());
        ghlRequest.setShouldSendAlertEmailsToAssignedMember(request.getShouldSendAlertEmailsToAssignedMember());
        ghlRequest.setAlertEmail(request.getAlertEmail());
        ghlRequest.setGoogleInvitationEmails(request.getGoogleInvitationEmails());
        ghlRequest.setAllowReschedule(request.getAllowReschedule());
        ghlRequest.setAllowCancellation(request.getAllowCancellation());
        ghlRequest.setShouldAssignContactToTeamMember(request.getShouldAssignContactToTeamMember());
        ghlRequest.setShouldSkipAssigningContactForExisting(request.getShouldSkipAssigningContactForExisting());
        ghlRequest.setNotes(request.getNotes());
        ghlRequest.setPixelId(request.getPixelId());
        ghlRequest.setFormSubmitType(request.getFormSubmitType());
        ghlRequest.setFormSubmitRedirectURL(request.getFormSubmitRedirectURL());
        ghlRequest.setFormSubmitThanksMessage(request.getFormSubmitThanksMessage());
        ghlRequest.setAvailabilityType(request.getAvailabilityType());

        ghlRequest.setAvailabilities(request.getAvailabilities() != null
                ? request.getAvailabilities().stream()
                .map(GoHighLevelCalendarRequest.Availability::fromAvailabilityRequest)
                .collect(Collectors.toList())
                : null);

        ghlRequest.setGuestType(request.getGuestType());
        ghlRequest.setConsentLabel(request.getConsentLabel());
        ghlRequest.setCalendarCoverImage(request.getCalendarCoverImage());
        ghlRequest.setLookBusyConfig(request.getLookBusyConfig() != null
                ? GoHighLevelCalendarRequest.LookBusyConfig.fromLookBusyConfigRequest(request.getLookBusyConfig())
                : null);

        return ghlRequest;
    }


    // ---------------- Inner Classes ----------------

    @Data
    public static class TeamMember {
        private String userId;
        private Double priority;
        private Boolean isPrimary;
        private List<LocationConfiguration> locationConfigurations;

        public static TeamMember fromTeamMemberRequest(org.example.loficonnect.dto.request.calendar.TeamMemberRequest request) {
            TeamMember teamMember = new TeamMember();
            teamMember.setUserId(request.getUserId());
            teamMember.setPriority(request.getPriority());
            teamMember.setIsPrimary(request.getIsPrimary());
            teamMember.setLocationConfigurations(request.getLocationConfigurations() != null
                    ? request.getLocationConfigurations().stream()
                    .map(LocationConfiguration::fromLocationConfigurationRequest)
                    .collect(Collectors.toList()) : null);
            return teamMember;
        }
    }

    @Data
    public static class LocationConfiguration {
        private String kind;
        private String location;

        public static LocationConfiguration fromLocationConfigurationRequest(org.example.loficonnect.dto.request.calendar.LocationConfigurationRequest request) {
            LocationConfiguration locationConfiguration = new LocationConfiguration();
            locationConfiguration.setKind(request.getKind());
            locationConfiguration.setLocation(request.getLocation());
            return locationConfiguration;
        }
    }

    @Data
    public static class OpenHours {
        private List<Integer> daysOfTheWeek;
        private List<OpenHour> hours;

        public static OpenHours fromOpenHoursRequest(org.example.loficonnect.dto.request.calendar.OpenHoursRequest request) {
            OpenHours openHours = new OpenHours();
            openHours.setDaysOfTheWeek(request.getDaysOfTheWeek());
            openHours.setHours(request.getOpenHours() != null
                    ? request.getOpenHours().stream()
                    .map(OpenHour::fromOpenHourRequest)
                    .collect(Collectors.toList())
                    : null);
            return openHours;
        }

        @Data
        public static class OpenHour {
            private int openHour;
            private int openMinute;
            private int closeHour;
            private int closeMinute;

            public static OpenHour fromOpenHourRequest(org.example.loficonnect.dto.request.calendar.OpenHourRequest request) {
                OpenHour openHour = new OpenHour();
                openHour.setOpenHour(request.getOpenHour());
                openHour.setOpenMinute(request.getOpenMinute());
                openHour.setCloseHour(request.getCloseHour());
                openHour.setCloseMinute(request.getCloseMinute());
                return openHour;
            }
        }
    }

    @Data
    public static class Recurring {
        private String freq;
        private int count;
        private String bookingOption;
        private String bookingOverlapDefaultStatus;

        public static Recurring fromRecurringRequest(org.example.loficonnect.dto.request.calendar.RecurringRequest request) {
            Recurring recurring = new Recurring();
            recurring.setFreq(request.getFreq());
            recurring.setCount(request.getCount());
            recurring.setBookingOption(request.getBookingOption());
            recurring.setBookingOverlapDefaultStatus(request.getBookingOverlapDefaultStatus());
            return recurring;
        }
    }

    @Data
    public static class Availability {
        private String date;
        private List<AvailabilityHour> hours;
        private Boolean deleted;

        public static Availability fromAvailabilityRequest(org.example.loficonnect.dto.request.calendar.AvailabilityRequest request) {
            Availability availability = new Availability();
            availability.setDeleted(request.getDeleted());
            if (request.getDate() != null) {
                availability.setDate(ZonedDateTime.parse(request.getDate()).format(DateTimeFormatter.ISO_INSTANT));
            }
            availability.setHours(request.getHours() != null
                    ? request.getHours().stream()
                    .map(AvailabilityHour::fromAvailabilityHourRequest)
                    .collect(Collectors.toList()) : null);
            return availability;
        }
    }

    @Data
    public static class AvailabilityHour {
        private Integer openHour;
        private Integer openMinute;
        private Integer closeHour;
        private Integer closeMinute;

        public static AvailabilityHour fromAvailabilityHourRequest(org.example.loficonnect.dto.request.calendar.AvailabilityHourRequest request) {
            AvailabilityHour availabilityHour = new AvailabilityHour();
            availabilityHour.setOpenHour(request.getOpenHour());
            availabilityHour.setOpenMinute(request.getOpenMinute());
            availabilityHour.setCloseHour(request.getCloseHour());
            availabilityHour.setCloseMinute(request.getCloseMinute());
            return availabilityHour;
        }
    }

    @Data
    public static class LookBusyConfig {
        private Boolean enabled;
        private Integer lookBusyPercentage;

        public static LookBusyConfig fromLookBusyConfigRequest(org.example.loficonnect.dto.request.calendar.LookBusyConfigRequest request) {
            LookBusyConfig config = new LookBusyConfig();
            config.setEnabled(request.getEnabled());
            config.setLookBusyPercentage(request.getLookBusyPercentage());
            return config;
        }
    }

    public void copyTo(GoHighLevelCalendarRequest target) {
        target.setGroupId(this.getGroupId());
        target.setTeamMembers(this.getTeamMembers());
        target.setEventType(this.getEventType());
        target.setName(this.getName());
        target.setDescription(this.getDescription());
        target.setSlug(this.getSlug());
        target.setWidgetSlug(this.getWidgetSlug());
        target.setWidgetType(this.getWidgetType());
        target.setEventTitle(this.getEventTitle());
        target.setEventColor(this.getEventColor());
        target.setLocationConfigurations(this.getLocationConfigurations());
        target.setSlotDuration(this.getSlotDuration());
        target.setSlotDurationUnit(this.getSlotDurationUnit());
        target.setSlotInterval(this.getSlotInterval());
        target.setSlotIntervalUnit(this.getSlotIntervalUnit());
        target.setSlotBuffer(this.getSlotBuffer());
        target.setSlotBufferUnit(this.getSlotBufferUnit());
        target.setPreBuffer(this.getPreBuffer());
        target.setPreBufferUnit(this.getPreBufferUnit());
        target.setAppointmentPerSlot(this.getAppointmentPerSlot());
        target.setAppointmentPerDay(this.getAppointmentPerDay());
        target.setAllowBookingAfter(this.getAllowBookingAfter());
        target.setAllowBookingAfterUnit(this.getAllowBookingAfterUnit());
        target.setAllowBookingFor(this.getAllowBookingFor());
        target.setAllowBookingForUnit(this.getAllowBookingForUnit());
        target.setOpenHours(this.getOpenHours());
        target.setEnableRecurring(this.getEnableRecurring());
        target.setRecurring(this.getRecurring());
        target.setFormId(this.getFormId());
        target.setStickyContact(this.getStickyContact());
        target.setIsLivePaymentMode(this.getIsLivePaymentMode());
        target.setAutoConfirm(this.getAutoConfirm());
        target.setShouldSendAlertEmailsToAssignedMember(this.getShouldSendAlertEmailsToAssignedMember());
        target.setAlertEmail(this.getAlertEmail());
        target.setGoogleInvitationEmails(this.getGoogleInvitationEmails());
        target.setAllowReschedule(this.getAllowReschedule());
        target.setAllowCancellation(this.getAllowCancellation());
        target.setShouldAssignContactToTeamMember(this.getShouldAssignContactToTeamMember());
        target.setShouldSkipAssigningContactForExisting(this.getShouldSkipAssigningContactForExisting());
        target.setNotes(this.getNotes());
        target.setPixelId(this.getPixelId());
        target.setFormSubmitType(this.getFormSubmitType());
        target.setFormSubmitRedirectURL(this.getFormSubmitRedirectURL());
        target.setFormSubmitThanksMessage(this.getFormSubmitThanksMessage());
        target.setAvailabilityType(this.getAvailabilityType());
        target.setAvailabilities(this.getAvailabilities());
        target.setGuestType(this.getGuestType());
        target.setConsentLabel(this.getConsentLabel());
        target.setCalendarCoverImage(this.getCalendarCoverImage());
        target.setLookBusyConfig(this.getLookBusyConfig());
    }

}
