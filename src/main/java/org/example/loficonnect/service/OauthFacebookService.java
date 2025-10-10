package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.oauthfacebook.AttachFacebookPageRequest;

import java.util.Map;

public interface OauthFacebookService {
    JsonNode startOauthFacebook(Map<String, Object> queryParams);
    JsonNode getFacebookPages(String locationId, String accountId);
    JsonNode attachFacebookPage(String locationId, String accountId, AttachFacebookPageRequest request);
}
