package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface SurveyService {
    JsonNode getSurveySubmissions(Map<String, Object> queryParams);
    JsonNode getSurveys(Map<String, Object> queryParams);
}
