package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.workflow.GoHighLevelContactWorkflowAddRequest;
import org.example.loficonnect.dto.mapper.workflow.GoHighLevelContactWorkflowDeleteRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
import org.example.loficonnect.feignclients.WorkflowClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.WorkflowService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {
    private final AuthorizationService authorizationService;
    private final WorkflowClient workflowClient;

    public WorkflowServiceImpl(AuthorizationService authorizationService, WorkflowClient workflowClient) {
        this.authorizationService = authorizationService;
        this.workflowClient = workflowClient;
    }

    @Override
    public JsonNode addContactToWorkflow(String contactId, String workflowId, ContactWorkflowAddRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactWorkflowAddRequest ghlRequest = GoHighLevelContactWorkflowAddRequest.fromRequest(request);
        return workflowClient.addContactToWorkflow(accessKey, version, contactId, workflowId, ghlRequest);
    }

    @Override
    public JsonNode deleteContactFromWorkflow(String contactId, String workflowId, ContactWorkflowDeleteRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactWorkflowDeleteRequest ghlRequest = GoHighLevelContactWorkflowDeleteRequest.fromRequest(request);
        return workflowClient.deleteContactFromWorkflow(accessKey, version, contactId, workflowId, ghlRequest);
    }
}
