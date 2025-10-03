package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface PipelineService {
    JsonNode getPipelines(Map<String, Object> queryParams);
}
