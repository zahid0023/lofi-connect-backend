package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.usersearch.UserSearchRequest;

import java.util.Map;

public interface UserSearchService {
    JsonNode searchUsers(Map<String, Object> queryParams);
    JsonNode filterUsersByEmail(UserSearchRequest request);
}
