package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
import org.example.loficonnect.service.WorkflowsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class WorkflowsController {
    private final WorkflowsService workflowsService;

    public WorkflowsController(WorkflowsService workflowsService) {
        this.workflowsService = workflowsService;
    }

    @AppKey
    @GetMapping("/workflows")
    public ResponseEntity<?> getWorkflows(@RequestParam("location-id") String locationId) {
        return ResponseEntity.ok(workflowsService.getWorkflows(locationId));
    }

    @AppKey
    @PostMapping("/contacts/{contact-id}/workflow/{workflow-id}")
    public ResponseEntity<?> addContactToWorkflow(
            @PathVariable("contact-id") String contactId,
            @PathVariable("workflow-id") String workflowId,
            @RequestBody ContactWorkflowAddRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workflowsService.addContactToWorkflow(contactId, workflowId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/workflow/{workflow-id}")
    public ResponseEntity<?> deleteContactFromWorkflow(
            @PathVariable("contact-id") String contactId,
            @PathVariable("workflow-id") String workflowId,
            @RequestBody ContactWorkflowDeleteRequest request) {
        return ResponseEntity.ok(workflowsService.deleteContactFromWorkflow(contactId, workflowId, request));
    }
}