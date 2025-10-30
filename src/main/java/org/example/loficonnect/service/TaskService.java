package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.task.TaskCompletedRequest;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;
import org.example.loficonnect.dto.request.task.TaskUpdateRequest;

public interface TaskService {
    JsonNode getContactTasks(String contactId);

    JsonNode createTask(String contactId, TaskCreateRequest request);

    JsonNode getContactTask(String contactId, String taskId);

    JsonNode updateTask(String contactId, String taskId, TaskUpdateRequest request);

    JsonNode deleteTask(String contactId, String taskId);

    JsonNode updateTaskCompleted(String contactId, String taskId, TaskCompletedRequest request);


}
