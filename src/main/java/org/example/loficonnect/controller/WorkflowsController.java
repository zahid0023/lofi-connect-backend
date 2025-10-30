package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.WorkflowsService;
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
    public ResponseEntity<?> getWorkflows(@RequestParam("locationId") String locationId) {
        return ResponseEntity.ok(workflowsService.getWorkflows(locationId));
    }
}