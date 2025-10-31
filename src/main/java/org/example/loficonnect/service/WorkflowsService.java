package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;

public interface WorkflowsService {
    JsonNode getWorkflows();

    JsonNode addContactToWorkflow(String contactId, String workflowId, ContactWorkflowAddRequest request);

    JsonNode deleteContactFromWorkflow(String contactId, String workflowId, ContactWorkflowDeleteRequest request);
}
