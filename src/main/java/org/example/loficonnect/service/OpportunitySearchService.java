package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface OpportunitySearchService {
    JsonNode searchOpportunities(Map<String, Object> queryParams);
}
