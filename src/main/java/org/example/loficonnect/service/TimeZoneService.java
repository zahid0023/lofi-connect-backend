package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface TimeZoneService {
    JsonNode getTimezones(String locationId);
}
