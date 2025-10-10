package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface CategoryService {
    JsonNode getCategories(String locationId, Map<String, Object> queryParams);
    JsonNode getCategoryById(String locationId, String categoryId);
}
