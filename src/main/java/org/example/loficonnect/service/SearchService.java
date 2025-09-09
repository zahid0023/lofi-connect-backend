package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;
import org.example.loficonnect.dto.request.search.TaskSearchRequest;

import java.util.Map;

public interface SearchService {
    JsonNode searchContacts(ContactSearchRequest request);

    JsonNode getDuplicateContacts(Map<String, Object> queryParams);

    JsonNode searchLocations(Map<String, Object> queryParams);

    JsonNode searchTasks(String locationId, TaskSearchRequest request);

    JsonNode searchConversations(Map<String, Object> queryParams);



}
