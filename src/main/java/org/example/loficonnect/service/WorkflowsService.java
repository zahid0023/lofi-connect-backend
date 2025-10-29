package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface WorkflowsService {
    JsonNode getWorkflows(String locationId);
}
