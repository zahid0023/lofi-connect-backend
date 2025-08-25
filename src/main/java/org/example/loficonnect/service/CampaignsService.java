package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.mapper.campaigns.ContactCampaignAssignRequest;

import java.util.Map;

public interface CampaignsService {
    JsonNode assignContactToCampaign(String contactId, String campaignId, ContactCampaignAssignRequest request);
    JsonNode removeContactFromCampaign(String contactId, String campaignId, Map<String, Object> queryParams);
    JsonNode removeAllCampaignsFromContact(String contactId, Map<String, Object> queryParams);



}
