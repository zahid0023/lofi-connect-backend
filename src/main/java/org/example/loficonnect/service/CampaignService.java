package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface CampaignService {
    JsonNode getCampaigns(Map<String, Object> queryParams);
}
