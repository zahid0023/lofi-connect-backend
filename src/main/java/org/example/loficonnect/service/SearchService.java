package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;

import java.util.Map;

public interface SearchService {
    JsonNode searchContacts(ContactSearchRequest request);

    JsonNode getDuplicateContacts(Map<String, Object> queryParams);



}
