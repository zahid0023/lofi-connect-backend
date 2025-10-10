package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateCreateRequest;
import org.example.loficonnect.service.InvoiceTemplateService;
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
class InvoiceTemplateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvoiceTemplateService invoiceTemplateService;

    @InjectMocks
    private InvoiceTemplateController invoiceTemplateController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceTemplateController).build();
    }

    @Test
    void testCreateInvoiceTemplate() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("name", "Invoice Template 1");

        when(invoiceTemplateService.createInvoiceTemplate(any(InvoiceTemplateCreateRequest.class))).thenReturn(mockResponse);

        String requestBody = """
            {
                "alt_id": "abc-123",
                "alt_type": "custom",
                "internal": true,
                "name": "Invoice Template 1",
                "business_details": {
                    "logo_url": "https://logo.com",
                    "name": "Acme Inc.",
                    "phone_no": "1234567890",
                    "address": "123 Main Street",
                    "website": "https://acme.com",
                    "custom_values": ["value1", "value2"]
                },
                "currency": "USD",
                "items": [],
                "automatic_taxes_enabled": true,
                "discount": {
                    "value": 10,
                    "type": "percentage",
                    "valid_on_product_ids": ["prod-1"]
                },
                "terms_notes": "Thank you for your business.",
                "title": "Standard Invoice",
                "tips_configuration": {
                    "tips_percentage": [10, 15],
                    "tips_enabled": true
                },
                "late_fees_configuration": {
                    "enable": true,
                    "value": 5,
                    "type": "percentage",
                    "frequency": {
                        "interval_count": 1,
                        "interval": "week"
                    },
                    "grace": {
                        "interval_count": 2,
                        "interval": "days"
                    },
                    "max_late_fees": {
                        "type": "percentage",
                        "value": "20"
                    }
                },
                "invoice_number_prefix": "INV-",
                "payment_methods": {
                    "stripe": {
                        "enable_bank_debit_only": true
                    }
                },
                "attachments": ["https://doc1.com"]
            }
        """;

        mockMvc.perform(post("/api/v1/template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"name\": \"Invoice Template 1\" }"));

        verify(invoiceTemplateService, times(1)).createInvoiceTemplate(any(InvoiceTemplateCreateRequest.class));
    }

    @Test
    void testListInvoiceTemplates() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 2);

        when(invoiceTemplateService.listInvoiceTemplates(anyMap())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/template")
                        .param("alt-id", "abc-123")
                        .param("alt-type", "custom")
                        .param("limit", "5")
                        .param("offset", "0")
                        .param("start-at", "2023-01-01")
                        .param("end-at", "2023-12-31")
                        .param("payment-mode", "stripe")
                        .param("status", "active")
                        .param("search", "invoice-001"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"total\": 2 }"));

        verify(invoiceTemplateService, times(1)).listInvoiceTemplates(argThat(params ->
                "abc-123".equals(params.get("altId")) &&
                        "custom".equals(params.get("altType")) &&
                        "5".equals(params.get("limit")) &&
                        "0".equals(params.get("offset")) &&
                        "2023-01-01".equals(params.get("startAt")) &&
                        "2023-12-31".equals(params.get("endAt")) &&
                        "stripe".equals(params.get("paymentMode")) &&
                        "active".equals(params.get("status")) &&
                        "invoice-001".equals(params.get("search"))
        ));
    }

    @Test
    void testGetInvoiceTemplate() throws Exception {
        String templateId = "tmpl-001";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("templateId", templateId);

        when(invoiceTemplateService.getInvoiceTemplate(eq(templateId), anyMap()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/template/{template-id}", templateId)
                        .param("alt-id", "abc-123")
                        .param("alt-type", "custom"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"templateId\": \"tmpl-001\" }"));

        verify(invoiceTemplateService, times(1)).getInvoiceTemplate(eq(templateId), argThat(params ->
                "abc-123".equals(params.get("altId")) &&
                        "custom".equals(params.get("altType"))
        ));
    }

    @Test
    void testUpdateInvoiceTemplate() throws Exception {
        String templateId = "tmpl-001";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "updated");

        when(invoiceTemplateService.updateInvoiceTemplate(eq(templateId), any())).thenReturn(mockResponse);

        String requestBody = """
        {
            "alt_id": "abc-123",
            "alt_type": "custom",
            "internal": true,
            "name": "Updated Invoice Template",
            "business_details": {
                "logo_url": "https://logo.com",
                "name": "Acme Inc.",
                "phone_no": "1234567890",
                "address": "123 Main Street",
                "website": "https://acme.com",
                "custom_values": ["val1", "val2"]
            },
            "currency": "USD",
            "items": [],
            "discount": {
                "value": 10,
                "type": "percentage",
                "valid_on_product_ids": ["prod-1"]
            },
            "terms_notes": "Updated terms",
            "title": "Updated Title"
        }
    """;

        mockMvc.perform(put("/api/v1/template/{template-id}", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"updated\" }"));

        verify(invoiceTemplateService, times(1)).updateInvoiceTemplate(eq(templateId), any());
    }

    @Test
    void testDeleteInvoiceTemplate() throws Exception {
        String templateId = "tmpl-001";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(invoiceTemplateService.deleteInvoiceTemplate(eq(templateId), anyMap())).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/template/{template-id}", templateId)
                        .param("alt-id", "abc-123")
                        .param("alt-type", "custom"))
                .andExpect(status().isNoContent())
                .andExpect(content().json("{ \"status\": \"deleted\" }"));

        verify(invoiceTemplateService, times(1)).deleteInvoiceTemplate(eq(templateId), argThat(params ->
                "abc-123".equals(params.get("altId")) &&
                        "custom".equals(params.get("altType"))
        ));
    }

    @Test
    void testUpdateLateFeesConfiguration() throws Exception {
        String templateId = "tmpl-001";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "late-fees-updated");

        when(invoiceTemplateService.updateLateFeesConfiguration(eq(templateId), any())).thenReturn(mockResponse);

        String requestBody = """
        {
            "alt_id": "abc-123",
            "alt_type": "custom",
            "late_fees_configuration": {
                "enable": true,
                "value": 5.0,
                "type": "percentage",
                "frequency": {
                    "interval_count": 1,
                    "interval": "month"
                },
                "grace": {
                    "interval_count": 3,
                    "interval": "days"
                },
                "max_late_fees": {
                    "type": "percentage",
                    "value": "25"
                }
            }
        }
    """;

        mockMvc.perform(patch("/api/v1/template/{template-id}/late-fees-configuration", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"late-fees-updated\" }"));

        verify(invoiceTemplateService, times(1)).updateLateFeesConfiguration(eq(templateId), any());
    }

    @Test
    void testUpdatePaymentMethodsConfiguration() throws Exception {
        String templateId = "tmpl-001";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "payment-methods-updated");

        when(invoiceTemplateService.updatePaymentMethodsConfiguration(eq(templateId), any()))
                .thenReturn(mockResponse);

        String requestBody = """
        {
            "alt_id": "abc-123",
            "alt_type": "custom",
            "payment_methods": {
                "stripe": {
                    "enable_bank_debit_only": true
                }
            }
        }
    """;

        mockMvc.perform(patch("/api/v1/template/{template-id}/payment-methods-configuration", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"payment-methods-updated\" }"));

        verify(invoiceTemplateService, times(1)).updatePaymentMethodsConfiguration(eq(templateId), any());
    }

}
