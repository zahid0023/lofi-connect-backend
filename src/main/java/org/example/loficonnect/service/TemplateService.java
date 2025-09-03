package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface TemplateService {
    JsonNode getTemplates(String locationId, Map<String, Object> queryParams);

    JsonNode deleteTemplate(String locationId, String templateId);


}
