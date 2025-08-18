package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.task.TaskCompletedRequest;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;
import org.example.loficonnect.dto.request.task.TaskUpdateRequest;
import org.example.loficonnect.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @AppKey
    @GetMapping("/contacts/{contact-id}/tasks")
    public ResponseEntity<?> getContactTasks(@PathVariable("contact-id") String contactId) {
        return ResponseEntity.ok(taskService.getContactTasks(contactId));
    }

    @AppKey
    @PostMapping("/contacts/{contact-id}/tasks")
    public ResponseEntity<?> createTask(
            @PathVariable("contact-id") String contactId,
            @RequestParam(value = "timeZone", defaultValue = "UTC") String timeZone,
            @RequestBody TaskCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(contactId, request, timeZone));
    }

    @AppKey
    @GetMapping("/contacts/{contact-id}/tasks/{task-id}")
    public ResponseEntity<?> getContactTask(
            @PathVariable("contact-id") String contactId,
            @PathVariable("task-id") String taskId
    ) {
        return ResponseEntity.ok(taskService.getContactTask(contactId, taskId));
    }

    @AppKey
    @PutMapping("/contacts/{contact-id}/tasks/{task-id}")
    public ResponseEntity<?> updateTask(
            @PathVariable("contact-id") String contactId,
            @PathVariable("task-id") String taskId,
            @RequestParam(value = "timeZone", defaultValue = "UTC") String timeZone,
            @RequestBody TaskUpdateRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(contactId, taskId, request, timeZone));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/tasks/{task-id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable("contact-id") String contactId,
            @PathVariable("task-id") String taskId
    ) {
        return ResponseEntity.ok(taskService.deleteTask(contactId, taskId));
    }

    @AppKey
    @PutMapping("/contacts/{contact-id}/tasks/{task-id}/completed")
    public ResponseEntity<?> updateTaskCompleted(
            @PathVariable("contact-id") String contactId,
            @PathVariable("task-id") String taskId,
            @RequestBody TaskCompletedRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTaskCompleted(contactId, taskId, request));
    }


}
