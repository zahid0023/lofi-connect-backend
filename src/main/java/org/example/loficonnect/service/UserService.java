package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.user.UserCreateRequest;
import org.example.loficonnect.dto.request.user.UserUpdateRequest;

import java.util.Map;

public interface UserService {
    JsonNode getUserById(String userId);
    JsonNode updateUserById(String userId, UserUpdateRequest request);
    JsonNode deleteUserById(String userId);
    JsonNode getUsersByLocation(Map<String, Object> queryParams);
    JsonNode createUser(UserCreateRequest request);
}
