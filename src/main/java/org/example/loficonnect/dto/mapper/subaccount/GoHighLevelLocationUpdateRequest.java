package org.example.loficonnect.dto.mapper.subaccount;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.subaccount.LocationUpdateRequest;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelLocationUpdateRequest {

    @JsonAlias("name")
    private String name;

    @JsonAlias("phone")
    private String phone;

    @JsonAlias("company_id")
    private String companyId;

    @JsonAlias("address")
    private String address;

    @JsonAlias("city")
    private String city;

    @JsonAlias("state")
    private String state;

    @JsonAlias("country")
    private String country;

    @JsonAlias("postal_code")
    private String postalCode;

    @JsonAlias("website")
    private String website;

    @JsonAlias("timezone")
    private String timezone;

    @JsonAlias("prospect_info")
    private ProspectInfo prospectInfo;

    @JsonAlias("settings")
    private Settings settings;

    @JsonAlias("social")
    private Social social;

    @JsonAlias("twilio")
    private Twilio twilio;

    @JsonAlias("mailgun")
    private Mailgun mailgun;

    @JsonAlias("snapshot")
    private Snapshot snapshot;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProspectInfo {
        @JsonAlias("first_name")
        private String firstName;

        @JsonAlias("last_name")
        private String lastName;

        @JsonAlias("email")
        private String email;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Settings {
        @JsonAlias("allow_duplicate_contact")
        private Boolean allowDuplicateContact;

        @JsonAlias("allow_duplicate_opportunity")
        private Boolean allowDuplicateOpportunity;

        @JsonAlias("allow_facebook_name_merge")
        private Boolean allowFacebookNameMerge;

        @JsonAlias("disable_contact_timezone")
        private Boolean disableContactTimezone;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Social {
        @JsonAlias("facebook_url")
        private String facebookUrl;

        @JsonAlias("google_plus")
        private String googlePlus;

        @JsonAlias("linked_in")
        private String linkedIn;

        @JsonAlias("foursquare")
        private String foursquare;

        @JsonAlias("twitter")
        private String twitter;

        @JsonAlias("yelp")
        private String yelp;

        @JsonAlias("instagram")
        private String instagram;

        @JsonAlias("youtube")
        private String youtube;

        @JsonAlias("pinterest")
        private String pinterest;

        @JsonAlias("blog_rss")
        private String blogRss;

        @JsonAlias("google_places_id")
        private String googlePlacesId;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Twilio {
        @JsonAlias("sid")
        private String sid;

        @JsonAlias("auth_token")
        private String authToken;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Mailgun {
        @JsonAlias("api_key")
        private String apiKey;

        @JsonAlias("domain")
        private String domain;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Snapshot {
        @JsonAlias("id")
        private String id;

        @JsonAlias("override")
        private Boolean override;
    }

    private GoHighLevelLocationUpdateRequest() {
        // private constructor
    }

    public static GoHighLevelLocationUpdateRequest fromRequest(LocationUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelLocationUpdateRequest.class);
    }
}