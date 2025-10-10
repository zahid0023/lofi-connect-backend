package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.opportunityfollower.OpportunityFollowerRequest;

import java.util.Map;

public interface OpportunityFollowerService {
    JsonNode addFollowers(String opportunityId, OpportunityFollowerRequest request);
    JsonNode removeFollowers(String opportunityId, Map<String, Object> requestBody);
}
