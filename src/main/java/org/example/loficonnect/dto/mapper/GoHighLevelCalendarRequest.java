package org.example.loficonnect.dto.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.loficonnect.dto.request.calendar.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Skip null or empty fields
public class GoHighLevelCalendarRequest {

    private Boolean isActive;
    private List<Notification> notifications;
    private String locationId;
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
    private LookBusyConfig lookBusyConfig;

    public static GoHighLevelCalendarRequest fromCalendarRequest(CalendarRequest calendarRequest) {
        GoHighLevelCalendarRequest goHighLevelRequest = new GoHighLevelCalendarRequest();

        goHighLevelRequest.setIsActive(calendarRequest.getIsActive());

        goHighLevelRequest.setNotifications(calendarRequest.getNotifications() != null
                ? calendarRequest.getNotifications().stream()
                .map(Notification::fromNotificationRequest)
                .collect(Collectors.toList()) : null);

        goHighLevelRequest.setLocationId(calendarRequest.getLocationId());
        goHighLevelRequest.setGroupId(calendarRequest.getGroupId());

        goHighLevelRequest.setTeamMembers(calendarRequest.getTeamMembers() != null
                ? calendarRequest.getTeamMembers().stream()
                .map(TeamMember::fromTeamMemberRequest)
                .collect(Collectors.toList()) : null);

        goHighLevelRequest.setEventType(calendarRequest.getEventType());
        goHighLevelRequest.setName(calendarRequest.getName());
        goHighLevelRequest.setDescription(calendarRequest.getDescription());
        goHighLevelRequest.setSlug(calendarRequest.getSlug());
        goHighLevelRequest.setWidgetSlug(calendarRequest.getWidgetSlug());
        goHighLevelRequest.setCalendarType(calendarRequest.getCalendarType());
        goHighLevelRequest.setWidgetType(calendarRequest.getWidgetType());
        goHighLevelRequest.setEventTitle(calendarRequest.getEventTitle());
        goHighLevelRequest.setEventColor(calendarRequest.getEventColor());

        goHighLevelRequest.setLocationConfigurations(calendarRequest.getLocationConfigurations() != null
                ? calendarRequest.getLocationConfigurations().stream()
                .map(LocationConfiguration::fromLocationConfigurationRequest)
                .collect(Collectors.toList()) : null);

        goHighLevelRequest.setSlotDuration(calendarRequest.getSlotDuration());
        goHighLevelRequest.setSlotDurationUnit(calendarRequest.getSlotDurationUnit());
        goHighLevelRequest.setSlotInterval(calendarRequest.getSlotInterval());
        goHighLevelRequest.setSlotIntervalUnit(calendarRequest.getSlotIntervalUnit());
        goHighLevelRequest.setSlotBuffer(calendarRequest.getSlotBuffer());
        goHighLevelRequest.setSlotBufferUnit(calendarRequest.getSlotBufferUnit());
        goHighLevelRequest.setPreBuffer(calendarRequest.getPreBuffer());
        goHighLevelRequest.setPreBufferUnit(calendarRequest.getPreBufferUnit());

        goHighLevelRequest.setAppointmentPerSlot(calendarRequest.getAppointmentPerSlot());
        goHighLevelRequest.setAppointmentPerDay(calendarRequest.getAppointmentPerDay());

        goHighLevelRequest.setAllowBookingAfter(calendarRequest.getAllowBookingAfter());
        goHighLevelRequest.setAllowBookingAfterUnit(calendarRequest.getAllowBookingAfterUnit());
        goHighLevelRequest.setAllowBookingFor(calendarRequest.getAllowBookingFor());
        goHighLevelRequest.setAllowBookingForUnit(calendarRequest.getAllowBookingForUnit());

        goHighLevelRequest.setOpenHours(calendarRequest.getOpenHours() != null
                ? calendarRequest.getOpenHours().stream()
                .map(GoHighLevelCalendarRequest.OpenHours::fromOpenHoursRequest)
                .collect(Collectors.toList())
                : null);

        goHighLevelRequest.setEnableRecurring(calendarRequest.getEnableRecurring());

        goHighLevelRequest.setRecurring(calendarRequest.getRecurring() != null
                ? Recurring.fromRecurringRequest(calendarRequest.getRecurring()) : null);

        goHighLevelRequest.setFormId(calendarRequest.getFormId());
        goHighLevelRequest.setStickyContact(calendarRequest.getStickyContact());
        goHighLevelRequest.setIsLivePaymentMode(calendarRequest.getIsLivePaymentMode());
        goHighLevelRequest.setAutoConfirm(calendarRequest.getAutoConfirm());
        goHighLevelRequest.setShouldSendAlertEmailsToAssignedMember(calendarRequest.getShouldSendAlertEmailsToAssignedMember());
        goHighLevelRequest.setAlertEmail(calendarRequest.getAlertEmail());
        goHighLevelRequest.setGoogleInvitationEmails(calendarRequest.getGoogleInvitationEmails());
        goHighLevelRequest.setAllowReschedule(calendarRequest.getAllowReschedule());
        goHighLevelRequest.setAllowCancellation(calendarRequest.getAllowCancellation());
        goHighLevelRequest.setShouldAssignContactToTeamMember(calendarRequest.getShouldAssignContactToTeamMember());
        goHighLevelRequest.setShouldSkipAssigningContactForExisting(calendarRequest.getShouldSkipAssigningContactForExisting());
        goHighLevelRequest.setNotes(calendarRequest.getNotes());
        goHighLevelRequest.setPixelId(calendarRequest.getPixelId());
        goHighLevelRequest.setFormSubmitType(calendarRequest.getFormSubmitType());
        goHighLevelRequest.setFormSubmitRedirectURL(calendarRequest.getFormSubmitRedirectURL());
        goHighLevelRequest.setFormSubmitThanksMessage(calendarRequest.getFormSubmitThanksMessage());
        goHighLevelRequest.setAvailabilityType(calendarRequest.getAvailabilityType());

        goHighLevelRequest.setAvailabilities(calendarRequest.getAvailabilities() != null
                ? calendarRequest.getAvailabilities().stream()
                .map(Availability::fromAvailabilityRequest)
                .collect(Collectors.toList()) : null);

        goHighLevelRequest.setGuestType(calendarRequest.getGuestType());
        goHighLevelRequest.setConsentLabel(calendarRequest.getConsentLabel());
        goHighLevelRequest.setCalendarCoverImage(calendarRequest.getCalendarCoverImage());
        goHighLevelRequest.setLookBusyConfig(calendarRequest.getLookBusyConfig() != null
                ? LookBusyConfig.fromLookBusyConfigRequest(calendarRequest.getLookBusyConfig()) : null);

        return goHighLevelRequest;
    }

    // Nested classes for all DTOs

    @Data
    public static class Notification {
        private String type;
        private Boolean shouldSendToContact;
        private Boolean shouldSendToGuest;
        private Boolean shouldSendToUser;
        private Boolean shouldSendToSelectedUsers;
        private String selectedUsers;

        public static Notification fromNotificationRequest(NotificationRequest request) {
            Notification notification = new Notification();
            notification.setType(request.getType());
            notification.setShouldSendToContact(request.getShouldSendToContact());
            notification.setShouldSendToGuest(request.getShouldSendToGuest());
            notification.setShouldSendToUser(request.getShouldSendToUser());
            notification.setShouldSendToSelectedUsers(request.getShouldSendToSelectedUsers());
            notification.setSelectedUsers(request.getSelectedUsers());
            return notification;
        }
    }

    @Data
    public static class TeamMember {
        private String userId;
        private Double priority;
        private Boolean isPrimary;
        private List<LocationConfiguration> locationConfigurations;

        public static TeamMember fromTeamMemberRequest(TeamMemberRequest request) {
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

        public static LocationConfiguration fromLocationConfigurationRequest(LocationConfigurationRequest request) {
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

        public static OpenHours fromOpenHoursRequest(OpenHoursRequest request) {
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

            public static OpenHour fromOpenHourRequest(OpenHourRequest request) {
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

        public static Recurring fromRecurringRequest(RecurringRequest request) {
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

        public static Availability fromAvailabilityRequest(AvailabilityRequest request) {
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

        public static AvailabilityHour fromAvailabilityHourRequest(AvailabilityHourRequest request) {
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

        public static LookBusyConfig fromLookBusyConfigRequest(LookBusyConfigRequest request) {
            LookBusyConfig config = new LookBusyConfig();
            config.setEnabled(request.getEnabled());
            config.setLookBusyPercentage(request.getLookBusyPercentage());
            return config;
        }
    }
}
