package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.statictics.StaticticsRequest;

public interface StaticticsService {
    JsonNode getStatistics(String locationId, StaticticsRequest request);
}
