package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.task.TaskCompletedRequest;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;
import org.example.loficonnect.dto.request.task.TaskUpdateRequest;
import org.example.loficonnect.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testGetContactTasks() throws Exception {
        String contactId = "123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("task", "Test Task");

        when(taskService.getContactTasks(contactId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/contacts/{contact-id}/tasks", contactId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"task\": \"Test Task\" }"));

        verify(taskService, times(1)).getContactTasks(contactId);
    }

    @Test
    void testCreateTask() throws Exception {
        String contactId = "123";
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setBody("Task body content");
        request.setDue_date(LocalDate.now());
        request.setDue_time(LocalTime.now());
        request.setTimeZone("UTC");
        request.setCompleted(false);
        request.setAssigned_to("user1");

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "success");

        when(taskService.createTask(eq(contactId), any(TaskCreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/contacts/{contact-id}/tasks", contactId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content("{ \"title\": \"Test Task\", \"body\": \"Task body content\", \"due_date\": \"2025-10-15\", \"due_time\": \"12:00\", \"timeZone\": \"UTC\", \"completed\": false, \"assigned_to\": \"user1\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"status\": \"success\" }"));

        verify(taskService, times(1)).createTask(eq(contactId), any(TaskCreateRequest.class));

    }

    @Test
    void testGetContactTask() throws Exception {
        String contactId = "123";
        String taskId = "456";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("task", "Test Task");

        when(taskService.getContactTask(contactId, taskId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/contacts/{contact-id}/tasks/{task-id}", contactId, taskId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"task\": \"Test Task\" }"));

        verify(taskService, times(1)).getContactTask(contactId, taskId);
    }

    @Test
    void testUpdateTask() throws Exception {
        String contactId = "123";
        String taskId = "456";
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");
        request.setBody("Updated task body");
        request.setDue_date(LocalDate.now());
        request.setDue_time(LocalTime.of(12, 0));
        request.setCompleted(true);
        request.setAssigned_to("user2");

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "updated");

        when(taskService.updateTask(eq(contactId), eq(taskId), any(TaskUpdateRequest.class), eq("UTC"))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/contacts/{contact-id}/tasks/{task-id}", contactId, taskId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .param("timeZone", "UTC")
                        .contentType("application/json")
                        .content("{ \"title\": \"Updated Task\", \"body\": \"Updated task body\", \"due_date\": \"2025-10-15\", \"due_time\": \"12:00\", \"completed\": true, \"assigned_to\": \"user2\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"updated\" }"));

        verify(taskService, times(1)).updateTask(eq(contactId), eq(taskId), any(TaskUpdateRequest.class), eq("UTC"));
    }

    @Test
    void testDeleteTask() throws Exception {
        String contactId = "Lq95Y9oQzB4KojmsjUxs";
        String taskId = "SeYUv2UoMkPT62MJF1ST";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(taskService.deleteTask(contactId, taskId)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/contacts/{contact-id}/tasks/{task-id}", contactId, taskId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"deleted\" }"));

        verify(taskService, times(1)).deleteTask(contactId, taskId);
    }

    @Test
    void testUpdateTaskCompleted() throws Exception {
        String contactId = "123";
        String taskId = "456";
        TaskCompletedRequest request = new TaskCompletedRequest();
        request.setCompleted(true);

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "completed");

        when(taskService.updateTaskCompleted(eq(contactId), eq(taskId), any(TaskCompletedRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/contacts/{contact-id}/tasks/{task-id}/completed", contactId, taskId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content("{ \"completed\": true }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"completed\" }"));

        verify(taskService, times(1)).updateTaskCompleted(eq(contactId), eq(taskId), any(TaskCompletedRequest.class));
    }
}
