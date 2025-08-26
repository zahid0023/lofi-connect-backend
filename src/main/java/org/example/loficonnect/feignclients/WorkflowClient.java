package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.workflow.GoHighLevelContactWorkflowAddRequest;
import org.example.loficonnect.dto.mapper.workflow.GoHighLevelContactWorkflowDeleteRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "workFlowClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface WorkflowClient {
    @PostMapping(
            value = "/contacts/{contactId}/workflow/{workflowId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode addContactToWorkflow(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("workflowId") String workflowId,
            @RequestBody GoHighLevelContactWorkflowAddRequest request
    );

    @DeleteMapping(
            value = "/contacts/{contactId}/workflow/{workflowId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteContactFromWorkflow(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contact-id") String contactId,
            @PathVariable("workflow-id") String workflowId,
            @RequestBody GoHighLevelContactWorkflowDeleteRequest request
    );


}
