package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.PipelineClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.PipelineService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PipelineServiceImpl implements PipelineService {

    private final PipelineClient pipelineClient;
    private final AuthorizationService authorizationService;

    public PipelineServiceImpl(PipelineClient pipelineClient, AuthorizationService authorizationService) {
        this.pipelineClient = pipelineClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getPipelines(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return pipelineClient.getPipelines(accessKey, version, queryParams);
    }
}
