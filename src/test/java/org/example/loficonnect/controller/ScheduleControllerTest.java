package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.schedule.ScheduleCreateRequest;
import org.example.loficonnect.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    void testCreateSchedule() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "scheduled");

        when(scheduleService.createSchedule(any(ScheduleCreateRequest.class))).thenReturn(mockResponse);

        String requestJson = """
            {
                "alt_id": "ALT123",
                "alt_type": "typeA",
                "name": "Test Schedule"
            }
        """;

        mockMvc.perform(post("/api/v1/schedules")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"scheduled\"}"));

        verify(scheduleService, times(1)).createSchedule(any(ScheduleCreateRequest.class));
    }


    @Test
    void testListSchedules() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("result", "success");

        when(scheduleService.listSchedules(
                eq("ALT123"),
                eq("typeA"),
                eq("10"),
                eq("0"),
                eq("2025-12-31"),
                eq("card"),
                eq("john"),
                eq("2025-01-01"),
                eq("active")
        )).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/schedules/list")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .param("alt-id", "ALT123")
                        .param("alt-type", "typeA")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("end-at", "2025-12-31")
                        .param("payment-mode", "card")
                        .param("search", "john")
                        .param("start-at", "2025-01-01")
                        .param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\": \"success\"}"));

        verify(scheduleService, times(1)).listSchedules(
                eq("ALT123"),
                eq("typeA"),
                eq("10"),
                eq("0"),
                eq("2025-12-31"),
                eq("card"),
                eq("john"),
                eq("2025-01-01"),
                eq("active")
        );
    }

    @Test
    void testGetSchedule() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("scheduleId", "sch-123");

        when(scheduleService.getSchedule("sch-123", "ALT123", "typeA")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/schedules/sch-123")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .param("alt-id", "ALT123")
                        .param("alt-type", "typeA"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"scheduleId\": \"sch-123\"}"));

        verify(scheduleService, times(1)).getSchedule("sch-123", "ALT123", "typeA");
    }

    @Test
    void testUpdateSchedule() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "updated");

        when(scheduleService.updateSchedule(eq("sch-123"), any())).thenReturn(mockResponse);

        String requestJson = """
            {
                "alt_id": "ALT123",
                "alt_type": "typeA",
                "name": "Updated Schedule"
            }
        """;

        mockMvc.perform(put("/api/v1/schedules/sch-123")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"updated\"}"));

        verify(scheduleService, times(1)).updateSchedule(eq("sch-123"), any());
    }

    @Test
    void testDeleteSchedule() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(scheduleService.deleteSchedule("sch-123", "ALT123", "typeA")).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/schedules/sch-123")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .param("alt-id", "ALT123")
                        .param("alt-type", "typeA"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"deleted\"}"));

        verify(scheduleService, times(1)).deleteSchedule("sch-123", "ALT123", "typeA");
    }

    @Test
    void testUpdateAndScheduleInvoice() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "scheduled");

        when(scheduleService.updateAndScheduleInvoice(eq("sch-123"), any())).thenReturn(mockResponse);

        String requestJson = """
            {
                "alt_id": "ALT123",
                "alt_type": "typeA",
                "name": "Updated & Scheduled"
            }
        """;

        mockMvc.perform(post("/api/v1/schedules/sch-123/update-and-schedule")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"scheduled\"}"));

        verify(scheduleService, times(1)).updateAndScheduleInvoice(eq("sch-123"), any());
    }

    @Test
    void testScheduleInvoice() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "scheduled");

        when(scheduleService.scheduleInvoice(eq("sch-123"), any())).thenReturn(mockResponse);

        String requestJson = """
            {
                "alt_id": "ALT123",
                "alt_type": "typeA",
                "live_mode": true,
                "auto_payment": {
                    "enable": true,
                    "type": "card",
                    "payment_method_id": "pm_123",
                    "customer_id": "cus_123",
                    "card": {
                        "brand": "visa",
                        "last4": "4242"
                    }
                }
            }
        """;

        mockMvc.perform(post("/api/v1/schedules/sch-123/schedule")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"scheduled\"}"));

        verify(scheduleService, times(1)).scheduleInvoice(eq("sch-123"), any());
    }

    @Test
    void testManageAutoPayment() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "auto-payment-updated");

        when(scheduleService.manageAutoPayment(eq("sch-123"), any())).thenReturn(mockResponse);

        String requestJson = """
            {
                "alt_id": "ALT123",
                "alt_type": "typeA",
                "id": "invoice-789",
                "auto_payment": {
                    "enable": true,
                    "type": "card",
                    "payment_method_id": "pm_123",
                    "customer_id": "cus_123",
                    "card": {
                        "brand": "visa",
                        "last4": "4242"
                    }
                }
            }
        """;

        mockMvc.perform(post("/api/v1/schedules/sch-123/auto-payment")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"auto-payment-updated\"}"));

        verify(scheduleService, times(1)).manageAutoPayment(eq("sch-123"), any());
    }

    @Test
    void testCancelScheduledInvoice() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "cancelled");

        when(scheduleService.cancelScheduledInvoice(eq("sch-123"), any())).thenReturn(mockResponse);

        String requestJson = """
            {
                "alt_id": "ALT123",
                "alt_type": "typeA"
            }
        """;

        mockMvc.perform(post("/api/v1/schedules/sch-123/cancel")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"cancelled\"}"));

        verify(scheduleService, times(1)).cancelScheduledInvoice(eq("sch-123"), any());
    }
}
