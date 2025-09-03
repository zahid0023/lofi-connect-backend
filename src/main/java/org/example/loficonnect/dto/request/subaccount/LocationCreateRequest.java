package org.example.loficonnect.dto.request.subaccount;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationCreateRequest {
    private String name;
    private String phone;
    private String companyId;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String website;
    private String timezone;
    private ProspectInfo prospectInfo;
    private Settings settings;
    private Social social;
    private Twilio twilio;
    private Mailgun mailgun;
    private String snapshotId;

    @Data
    public static class ProspectInfo {
        private String firstName;
        private String lastName;
        private String email;
    }

    @Data
    public static class Settings {
        private Boolean allowDuplicateContact;
        private Boolean allowDuplicateOpportunity;
        private Boolean allowFacebookNameMerge;
        private Boolean disableContactTimezone;
    }

    @Data
    public static class Social {
        private String facebookUrl;
        private String googlePlus;
        private String linkedIn;
        private String foursquare;
        private String twitter;
        private String yelp;
        private String instagram;
        private String youtube;
        private String pinterest;
        private String blogRss;
        private String googlePlacesId;
    }

    @Data
    public static class Twilio {
        private String sid;
        private String authToken;
    }

    @Data
    public static class Mailgun {
        private String apiKey;
        private String domain;
    }
}
