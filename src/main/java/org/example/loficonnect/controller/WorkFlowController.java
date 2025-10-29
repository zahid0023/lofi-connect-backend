package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
import org.example.loficonnect.service.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class WorkFlowController {
    private final WorkflowService workflowService;

    public WorkFlowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @AppKey
    @PostMapping("/contacts/{contactId}/workflow/{workflowId}")
    public ResponseEntity<?> addContactToWorkflow(
            @PathVariable("contactId") String contactId,
            @PathVariable("workflowId") String workflowId,
            @RequestBody ContactWorkflowAddRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workflowService.addContactToWorkflow(contactId, workflowId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/workflow/{workflow-id}")
    public ResponseEntity<?> deleteContactFromWorkflow(
            @PathVariable("contact-id") String contactId,
            @PathVariable("workflow-id") String workflowId,
            @RequestBody ContactWorkflowDeleteRequest request) {
        return ResponseEntity.ok(workflowService.deleteContactFromWorkflow(contactId, workflowId, request));
    }
}
