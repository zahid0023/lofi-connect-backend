package org.example.loficonnect.dto.mapper.subaccount;

import lombok.Data;
import org.example.loficonnect.dto.request.subaccount.LocationCreateRequest;

@Data
public class GoHighLevelLocationCreateRequest {
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

    public static GoHighLevelLocationCreateRequest fromRequest(LocationCreateRequest request) {
        GoHighLevelLocationCreateRequest ghlRequest = new GoHighLevelLocationCreateRequest();
        ghlRequest.setName(request.getName());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setCompanyId(request.getCompanyId());
        ghlRequest.setAddress(request.getAddress());
        ghlRequest.setCity(request.getCity());
        ghlRequest.setState(request.getState());
        ghlRequest.setCountry(request.getCountry());
        ghlRequest.setPostalCode(request.getPostalCode());
        ghlRequest.setWebsite(request.getWebsite());
        ghlRequest.setTimezone(request.getTimezone());

        if (request.getProspectInfo() != null) {
            ProspectInfo pi = new ProspectInfo();
            pi.setFirstName(request.getProspectInfo().getFirstName());
            pi.setLastName(request.getProspectInfo().getLastName());
            pi.setEmail(request.getProspectInfo().getEmail());
            ghlRequest.setProspectInfo(pi);
        }

        if (request.getSettings() != null) {
            Settings s = new Settings();
            s.setAllowDuplicateContact(request.getSettings().getAllowDuplicateContact());
            s.setAllowDuplicateOpportunity(request.getSettings().getAllowDuplicateOpportunity());
            s.setAllowFacebookNameMerge(request.getSettings().getAllowFacebookNameMerge());
            s.setDisableContactTimezone(request.getSettings().getDisableContactTimezone());
            ghlRequest.setSettings(s);
        }

        if (request.getSocial() != null) {
            Social social = new Social();
            social.setFacebookUrl(request.getSocial().getFacebookUrl());
            social.setGooglePlus(request.getSocial().getGooglePlus());
            social.setLinkedIn(request.getSocial().getLinkedIn());
            social.setFoursquare(request.getSocial().getFoursquare());
            social.setTwitter(request.getSocial().getTwitter());
            social.setYelp(request.getSocial().getYelp());
            social.setInstagram(request.getSocial().getInstagram());
            social.setYoutube(request.getSocial().getYoutube());
            social.setPinterest(request.getSocial().getPinterest());
            social.setBlogRss(request.getSocial().getBlogRss());
            social.setGooglePlacesId(request.getSocial().getGooglePlacesId());
            ghlRequest.setSocial(social);
        }

        if (request.getTwilio() != null) {
            Twilio t = new Twilio();
            t.setSid(request.getTwilio().getSid());
            t.setAuthToken(request.getTwilio().getAuthToken());
            ghlRequest.setTwilio(t);
        }

        if (request.getMailgun() != null) {
            Mailgun m = new Mailgun();
            m.setApiKey(request.getMailgun().getApiKey());
            m.setDomain(request.getMailgun().getDomain());
            ghlRequest.setMailgun(m);
        }

        ghlRequest.setSnapshotId(request.getSnapshotId());

        return ghlRequest;
    }
}
