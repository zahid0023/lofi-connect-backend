package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.task.GoHighLevelTaskCompletedRequest;
import org.example.loficonnect.dto.mapper.task.GoHighLevelTaskCreateRequest;
import org.example.loficonnect.dto.mapper.task.GoHighLevelTaskUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "taskClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TaskClient {
    @GetMapping(
            value = "/contacts/{contactId}/tasks",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getContactTasks(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId
    );

    @PostMapping(
            value = "/contacts/{contactId}/tasks",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createTask(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @RequestBody GoHighLevelTaskCreateRequest request
    );

    @GetMapping(
            value = "/contacts/{contactId}/tasks/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getContactTask(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("taskId") String taskId
    );

    @PutMapping(
            value = "/contacts/{contactId}/tasks/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateTask(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("taskId") String taskId,
            @RequestBody GoHighLevelTaskUpdateRequest request
    );

    @DeleteMapping(
            value = "/contacts/{contactId}/tasks/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteTask(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("taskId") String taskId
    );

    @PutMapping(
            value = "/contacts/{contactId}/tasks/{taskId}/completed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateTaskCompleted(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("taskId") String taskId,
            @RequestBody GoHighLevelTaskCompletedRequest request
    );


}
