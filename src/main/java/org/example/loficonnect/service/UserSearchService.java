package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface UserSearchService {
    JsonNode searchUsers(Map<String, Object> queryParams);
}
