package org.example.loficonnect.dto.mapper.subaccount;

import lombok.Data;
import org.example.loficonnect.dto.request.subaccount.LocationUpdateRequest;

@Data
public class GoHighLevelLocationUpdateRequest {
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
    private Snapshot snapshot;

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

    @Data
    public static class Snapshot {
        private String id;
        private Boolean override;
    }

    public static GoHighLevelLocationUpdateRequest fromRequest(LocationUpdateRequest request) {
        GoHighLevelLocationUpdateRequest ghl = new GoHighLevelLocationUpdateRequest();
        ghl.setName(request.getName());
        ghl.setPhone(request.getPhone());
        ghl.setCompanyId(request.getCompanyId());
        ghl.setAddress(request.getAddress());
        ghl.setCity(request.getCity());
        ghl.setState(request.getState());
        ghl.setCountry(request.getCountry());
        ghl.setPostalCode(request.getPostalCode());
        ghl.setWebsite(request.getWebsite());
        ghl.setTimezone(request.getTimezone());

        if (request.getProspectInfo() != null) {
            ProspectInfo pi = new ProspectInfo();
            pi.setFirstName(request.getProspectInfo().getFirstName());
            pi.setLastName(request.getProspectInfo().getLastName());
            pi.setEmail(request.getProspectInfo().getEmail());
            ghl.setProspectInfo(pi);
        }

        if (request.getSettings() != null) {
            Settings s = new Settings();
            s.setAllowDuplicateContact(request.getSettings().getAllowDuplicateContact());
            s.setAllowDuplicateOpportunity(request.getSettings().getAllowDuplicateOpportunity());
            s.setAllowFacebookNameMerge(request.getSettings().getAllowFacebookNameMerge());
            s.setDisableContactTimezone(request.getSettings().getDisableContactTimezone());
            ghl.setSettings(s);
        }

        if (request.getSocial() != null) {
            Social s = new Social();
            s.setFacebookUrl(request.getSocial().getFacebookUrl());
            s.setGooglePlus(request.getSocial().getGooglePlus());
            s.setLinkedIn(request.getSocial().getLinkedIn());
            s.setFoursquare(request.getSocial().getFoursquare());
            s.setTwitter(request.getSocial().getTwitter());
            s.setYelp(request.getSocial().getYelp());
            s.setInstagram(request.getSocial().getInstagram());
            s.setYoutube(request.getSocial().getYoutube());
            s.setPinterest(request.getSocial().getPinterest());
            s.setBlogRss(request.getSocial().getBlogRss());
            s.setGooglePlacesId(request.getSocial().getGooglePlacesId());
            ghl.setSocial(s);
        }

        if (request.getTwilio() != null) {
            Twilio t = new Twilio();
            t.setSid(request.getTwilio().getSid());
            t.setAuthToken(request.getTwilio().getAuthToken());
            ghl.setTwilio(t);
        }

        if (request.getMailgun() != null) {
            Mailgun m = new Mailgun();
            m.setApiKey(request.getMailgun().getApiKey());
            m.setDomain(request.getMailgun().getDomain());
            ghl.setMailgun(m);
        }

        if (request.getSnapshot() != null) {
            Snapshot sn = new Snapshot();
            sn.setId(request.getSnapshot().getId());
            sn.setOverride(request.getSnapshot().getOverride());
            ghl.setSnapshot(sn);
        }

        return ghl;
    }
}
