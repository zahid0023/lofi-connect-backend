package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.task.GoHighLevelTaskCompletedRequest;
import org.example.loficonnect.dto.mapper.task.GoHighLevelTaskCreateRequest;
import org.example.loficonnect.dto.mapper.task.GoHighLevelTaskUpdateRequest;
import org.example.loficonnect.dto.request.task.TaskCompletedRequest;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;
import org.example.loficonnect.dto.request.task.TaskUpdateRequest;
import org.example.loficonnect.feignclients.TaskClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TaskService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final AuthorizationService authorizationService;
    private final TaskClient taskClient;
    private final ObjectMapper objectMapper;

    public TaskServiceImpl(AuthorizationService authorizationService, TaskClient taskClient, ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.taskClient = taskClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getContactTasks(String contactId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return taskClient.getContactTasks(accessKey, version, contactId);
    }

    @Override
    public JsonNode createTask(String contactId, TaskCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTaskCreateRequest ghlRequest = GoHighLevelTaskCreateRequest.fromRequest(request, objectMapper);
        return taskClient.createTask(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode getContactTask(String contactId, String taskId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return taskClient.getContactTask(accessKey, version, contactId, taskId);
    }

    @Override
    public JsonNode updateTask(String contactId, String taskId, TaskUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTaskUpdateRequest ghlRequest = GoHighLevelTaskUpdateRequest.fromRequest(request, objectMapper);
        return taskClient.updateTask(accessKey, version, contactId, taskId, ghlRequest);
    }

    @Override
    public JsonNode deleteTask(String contactId, String taskId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return taskClient.deleteTask(accessKey, version, contactId, taskId);
    }

    @Override
    public JsonNode updateTaskCompleted(String contactId, String taskId, TaskCompletedRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTaskCompletedRequest ghlRequest = GoHighLevelTaskCompletedRequest.fromRequest(request, objectMapper);
        return taskClient.updateTaskCompleted(accessKey, version, contactId, taskId, ghlRequest);
    }


}
