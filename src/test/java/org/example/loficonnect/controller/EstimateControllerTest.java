package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.estimate.*;
import org.example.loficonnect.service.EstimateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EstimateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EstimateService estimateService;

    @InjectMocks
    private EstimateController estimateController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estimateController).build();
    }

    @Test
    void testCreateEstimate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "estimate_created");

        when(estimateService.createEstimate(any(EstimateCreateRequest.class))).thenReturn(mockResponse);

        String requestBody = """
        {
            "alt_id": "abc123",
            "alt_type": "contact",
            "name": "Sample Estimate",
            "currency": "USD",
            "live_mode": true,
            "terms_notes": "Payment due in 7 days",
            "title": "Estimate for services",
            "estimate_number": 1001,
            "issue_date": "2025-10-11",
            "expiry_date": "2025-11-11",
            "automatic_taxes_enabled": false,
            "estimate_number_prefix": "EST-",
            "user_id": "user_001"
        }
        """;

        mockMvc.perform(post("/api/v1/estimate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\": \"estimate_created\"}"));

        verify(estimateService, times(1)).createEstimate(any(EstimateCreateRequest.class));
    }

    @Test
    void testUpdateEstimate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "estimate_updated");

        when(estimateService.updateEstimate(eq("est_001"), any(EstimateUpdateRequest.class))).thenReturn(mockResponse);

        String requestBody = """
        {
            "alt_id": "abc123",
            "alt_type": "contact",
            "name": "Updated Estimate",
            "currency": "USD",
            "live_mode": true,
            "terms_notes": "Updated payment terms",
            "title": "Updated Title",
            "estimate_number": 2002,
            "issue_date": "2025-10-11",
            "expiry_date": "2025-12-31",
            "automatic_taxes_enabled": true,
            "estimate_number_prefix": "EST-",
            "user_id": "user_002",
            "estimate_status": "sent"
        }
        """;

        mockMvc.perform(put("/api/v1/estimate/est_001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"estimate_updated\"}"));

        verify(estimateService, times(1)).updateEstimate(eq("est_001"), any(EstimateUpdateRequest.class));
    }

    @Test
    void testDeleteEstimate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(estimateService.deleteEstimate("est_001")).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/estimate/est_001")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"status\": \"deleted\"}"));

        verify(estimateService, times(1)).deleteEstimate("est_001");
    }

    @Test
    void testGenerateEstimateNumber() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("number", "EST-1005");

        when(estimateService.generateEstimateNumber("abc123", "contact")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/estimate/generate-number")
                        .param("alt-id", "abc123")
                        .param("alt-type", "contact")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"number\": \"EST-1005\"}"));

        verify(estimateService, times(1)).generateEstimateNumber("abc123", "contact");
    }

    @Test
    void testSendEstimate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "sent");

        when(estimateService.sendEstimate(eq("est_001"), any(EstimateSendRequest.class))).thenReturn(mockResponse);

        String requestBody = """
    {
        "alt_id": "abc123",
        "alt_type": "contact",
        "action": "send_now",
        "live_mode": true,
        "user_id": "user_001",
        "sent_from": {
            "from_name": "John Doe",
            "from_email": "john@example.com"
        },
        "estimate_name": "Estimate #1005"
    }
    """;

        mockMvc.perform(post("/api/v1/estimate/est_001/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"sent\"}"));

        verify(estimateService, times(1)).sendEstimate(eq("est_001"), any(EstimateSendRequest.class));
    }

    @Test
    void testCreateInvoiceFromEstimate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "invoice_created");

        when(estimateService.createInvoiceFromEstimate(eq("est_001"), any(InvoiceCreateRequest.class))).thenReturn(mockResponse);

        String requestBody = """
    {
        "alt_id": "abc123",
        "alt_type": "contact",
        "mark_as_invoiced": true,
        "version": "1.0"
    }
    """;

        mockMvc.perform(post("/api/v1/estimate/est_001/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\": \"invoice_created\"}"));

        verify(estimateService, times(1)).createInvoiceFromEstimate(eq("est_001"), any(InvoiceCreateRequest.class));
    }

    @Test
    void testListEstimates() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("count", 2);

        when(estimateService.listEstimates(
                eq("abc123"), eq("contact"), eq("10"), eq("0"),
                eq("contact_001"), eq("2025-12-31"), eq("sample search"),
                eq("2025-01-01"), eq("sent")
        )).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/estimate/list")
                        .param("alt-id", "abc123")
                        .param("alt-type", "contact")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("contact-id", "contact_001")
                        .param("end-at", "2025-12-31")
                        .param("search", "sample search")
                        .param("start-at", "2025-01-01")
                        .param("status", "sent")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"count\": 2}"));

        verify(estimateService, times(1)).listEstimates(
                "abc123", "contact", "10", "0",
                "contact_001", "2025-12-31", "sample search",
                "2025-01-01", "sent"
        );
    }

    @Test
    void testUpdateEstimateLastVisitedAt() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "updated");

        when(estimateService.updateEstimateLastVisitedAt(any(LastVisitedUpdateRequest.class))).thenReturn(mockResponse);

        String requestBody = """
    {
        "estimate_id": "est_123"
    }
    """;

        mockMvc.perform(patch("/api/v1/estimate/stats/last-visited-at")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"updated\"}"));

        verify(estimateService, times(1)).updateEstimateLastVisitedAt(any(LastVisitedUpdateRequest.class));
    }

    @Test
    void testListEstimateTemplates() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 3);

        when(estimateService.listEstimateTemplates("abc123", "contact", "10", "0", "template"))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/estimate/templates")
                        .param("alt-id", "abc123")
                        .param("alt-type", "contact")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("search", "template")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\": 3}"));

        verify(estimateService, times(1))
                .listEstimateTemplates("abc123", "contact", "10", "0", "template");
    }

    @Test
    void testCreateEstimateTemplate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "template_created");

        when(estimateService.createEstimateTemplate(any(EstimateTemplateCreateRequest.class)))
                .thenReturn(mockResponse);

        String requestBody = """
    {
        "alt_id": "abc123",
        "alt_type": "contact",
        "name": "Template A",
        "currency": "USD",
        "live_mode": true,
        "terms_notes": "Terms and conditions apply",
        "title": "Consultation Template",
        "automatic_taxes_enabled": false,
        "estimate_number_prefix": "TEMP-",
        "business_details": {
            "name": "Business X",
            "phone_no": "123456789",
            "address": "123 Main St",
            "website": "https://biz.com",
            "logo_url": "https://logo.url",
            "custom_values": ["val1", "val2"]
        }
    }
    """;

        mockMvc.perform(post("/api/v1/estimate/template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\": \"template_created\"}"));

        verify(estimateService, times(1)).createEstimateTemplate(any(EstimateTemplateCreateRequest.class));
    }

    @Test
    void testUpdateEstimateTemplate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "template_updated");

        when(estimateService.updateEstimateTemplate(any(EstimateTemplateUpdateRequest.class), eq("temp_123")))
                .thenReturn(mockResponse);

        String requestBody = """
    {
        "alt_id": "abc123",
        "alt_type": "contact",
        "name": "Updated Template",
        "currency": "USD",
        "live_mode": true,
        "terms_notes": "Updated terms",
        "title": "Updated Title",
        "automatic_taxes_enabled": true,
        "estimate_number_prefix": "TEMP-NEW",
        "business_details": {
            "name": "Business Y",
            "phone_no": "987654321",
            "address": "456 Main St",
            "website": "https://example.com",
            "logo_url": "https://logo.example.com",
            "custom_values": ["c1", "c2"]
        }
    }
    """;

        mockMvc.perform(put("/api/v1/estimate/template/temp_123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"template_updated\"}"));

        verify(estimateService, times(1)).updateEstimateTemplate(any(EstimateTemplateUpdateRequest.class), eq("temp_123"));
    }

    @Test
    void testDeleteEstimateTemplate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "template_deleted");

        when(estimateService.deleteEstimateTemplate("temp_123", "abc123", "contact"))
                .thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/estimate/template/temp_123")
                        .param("alt-id", "abc123")
                        .param("alt-type", "contact")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"template_deleted\"}"));

        verify(estimateService, times(1)).deleteEstimateTemplate("temp_123", "abc123", "contact");
    }

    @Test
    void testPreviewEstimateTemplate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("preview", "success");

        when(estimateService.previewEstimateTemplate("abc123", "contact", "temp_123"))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/estimate/template/preview")
                        .param("alt-id", "abc123")
                        .param("alt-type", "contact")
                        .param("template-id", "temp_123")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"preview\": \"success\"}"));

        verify(estimateService, times(1)).previewEstimateTemplate("abc123", "contact", "temp_123");
    }

}
