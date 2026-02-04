package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
import org.example.loficonnect.service.WorkflowsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl/workflows")
public class WorkflowsController {
    private final WorkflowsService workflowsService;

    public WorkflowsController(WorkflowsService workflowsService) {
        this.workflowsService = workflowsService;
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getWorkflows() {
        return ResponseEntity.ok(workflowsService.getWorkflows());
    }

    @AppKey
    @PostMapping("/{contact-id}/{workflow-id}")
    public ResponseEntity<?> addContactToWorkflow(
            @PathVariable("contact-id") String contactId,
            @PathVariable("workflow-id") String workflowId,
            @RequestBody ContactWorkflowAddRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workflowsService.addContactToWorkflow(contactId, workflowId, request));
    }

    @AppKey
    @DeleteMapping("/{contact-id}/{workflow-id}")
    public ResponseEntity<?> deleteContactFromWorkflow(
            @PathVariable("contact-id") String contactId,
            @PathVariable("workflow-id") String workflowId,
            @RequestBody ContactWorkflowDeleteRequest request) {
        return ResponseEntity.ok(workflowsService.deleteContactFromWorkflow(contactId, workflowId, request));
    }
}