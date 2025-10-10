package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface FunnelService {
    JsonNode getFunnelList(Map<String, Object> queryParams);
    JsonNode getFunnelPages(Map<String, Object> queryParams);
    JsonNode getFunnelPageCount(Map<String, Object> queryParams);
}
