package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.SurveyClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.SurveyService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyClient surveyClient;
    private final AuthorizationService authorizationService;

    public SurveyServiceImpl(SurveyClient surveyClient, AuthorizationService authorizationService) {
        this.surveyClient = surveyClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getSurveySubmissions(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return surveyClient.getSurveySubmissions(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getSurveys(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return surveyClient.getSurveys(accessKey, version, queryParams);
    }

}
