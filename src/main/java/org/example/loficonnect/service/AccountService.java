package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface AccountService {
    JsonNode getAccounts(String locationId);
    JsonNode deleteAccount(String locationId, String id, Map<String, Object> queryParams);
}
