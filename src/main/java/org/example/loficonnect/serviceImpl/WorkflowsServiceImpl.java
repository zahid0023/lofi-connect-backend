package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.WorkflowsClient;
import org.example.loficonnect.service.WorkflowsService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class WorkflowsServiceImpl implements WorkflowsService {
    private final WorkflowsClient workflowsClient;
    private final AuthorizationService authorizationService;

    public WorkflowsServiceImpl(WorkflowsClient workflowsClient, AuthorizationService authorizationService) {
        this.workflowsClient = workflowsClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getWorkflows(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return workflowsClient.getWorkflows(accessKey, version, locationId);
    }
}
