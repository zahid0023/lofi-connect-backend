package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.workflow.GoHighLevelContactWorkflowAddRequest;
import org.example.loficonnect.dto.mapper.workflow.GoHighLevelContactWorkflowDeleteRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
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
    private final ObjectMapper objectMapper;

    public WorkflowsServiceImpl(WorkflowsClient workflowsClient,
                                AuthorizationService authorizationService,
                                ObjectMapper objectMapper) {
        this.workflowsClient = workflowsClient;
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getWorkflows(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return workflowsClient.getWorkflows(accessKey, version, locationId);
    }

    @Override
    public JsonNode addContactToWorkflow(String contactId, String workflowId, ContactWorkflowAddRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactWorkflowAddRequest ghlRequest = GoHighLevelContactWorkflowAddRequest.fromRequest(request);
        return workflowsClient.addContactToWorkflow(accessKey, version, contactId, workflowId, ghlRequest);
    }

    @Override
    public JsonNode deleteContactFromWorkflow(String contactId, String workflowId, ContactWorkflowDeleteRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactWorkflowDeleteRequest ghlRequest = GoHighLevelContactWorkflowDeleteRequest.fromRequest(request, objectMapper);
        return workflowsClient.deleteContactFromWorkflow(accessKey, version, contactId, workflowId, ghlRequest);
    }
}
