package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.invoice.*;
import org.example.loficonnect.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
    }

    @Test
    void testGenerateInvoiceNumber() throws Exception {
        String altId = "abc123";
        String altType = "contact";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("invoiceNumber", "INV-2025-001");

        when(invoiceService.generateInvoiceNumber(altId, altType)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/generate-invoice-number")
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"invoiceNumber\":\"INV-2025-001\"}"));

        verify(invoiceService, times(1)).generateInvoiceNumber(altId, altType);
    }

    @Test
    void testGetInvoice() throws Exception {
        String invoiceId = "inv-789";
        String altId = "abc123";
        String altType = "contact";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("id", invoiceId)
                .put("amount", 250.75)
                .put("status", "paid");

        when(invoiceService.getInvoice(invoiceId, altId, altType)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/invoices/{invoice-id}", invoiceId)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"inv-789\",\"amount\":250.75,\"status\":\"paid\"}"));

        verify(invoiceService, times(1)).getInvoice(invoiceId, altId, altType);
    }

    @Test
    void testUpdateInvoice() throws Exception {
        String invoiceId = "inv-999";

        String requestJson = """
    {
        "alt_id": "abc123",
        "alt_type": "contact",
        "name": "Updated Invoice",
        "currency": "USD",
        "invoice_number": "INV-999",
        "issue_date": "2025-10-10",
        "due_date": "2025-10-15",
        "automatic_taxes_enabled": true,
        "live_mode": false
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("id", invoiceId)
                .put("status", "updated");

        when(invoiceService.updateInvoice(eq(invoiceId), any(InvoiceUpdateRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/invoices/{invoice-id}", invoiceId)
                        .contentType("application/json")
                        .content(requestJson)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"inv-999\",\"status\":\"updated\"}"));

        verify(invoiceService, times(1)).updateInvoice(eq(invoiceId), any(InvoiceUpdateRequest.class));
    }

    @Test
    void testDeleteInvoice() throws Exception {
        String invoiceId = "inv-456";
        String altId = "alt-001";
        String altType = "contact";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("id", invoiceId)
                .put("status", "deleted");

        when(invoiceService.deleteInvoice(invoiceId, altId, altType)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/invoices/{invoice-id}", invoiceId)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"inv-456\",\"status\":\"deleted\"}"));

        verify(invoiceService, times(1)).deleteInvoice(invoiceId, altId, altType);
    }

    @Test
    void testUpdateLateFeesConfiguration() throws Exception {
        String invoiceId = "inv-888";
        String altId = "alt-002";
        String altType = "contact";

        String requestJson = """
    {
        "alt_id": "alt-002",
        "alt_type": "contact",
        "late_fees_configuration": {
            "enable": true,
            "value": 50,
            "type": "fixed",
            "frequency": {
                "interval_count": 1,
                "interval": "days"
            },
            "grace": {
                "interval_count": 2,
                "interval": "days"
            },
            "max_late_fees": {
                "type": "percentage",
                "value": "100"
            }
        }
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("invoice_id", invoiceId)
                .put("late_fees_status", "updated");

        when(invoiceService.updateLateFeesConfiguration(eq(invoiceId), eq(altId), eq(altType), any(InvoiceLateFeesConfigurationRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(patch("/api/v1/invoices/{invoice-id}/late-fees-configuration", invoiceId)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"invoice_id\":\"inv-888\",\"late_fees_status\":\"updated\"}"));

        verify(invoiceService, times(1)).updateLateFeesConfiguration(eq(invoiceId), eq(altId), eq(altType), any(InvoiceLateFeesConfigurationRequest.class));
    }

    @Test
    void testVoidInvoice() throws Exception {
        String invoiceId = "inv-321";
        String altId = "alt-999";
        String altType = "contact";

        String requestJson = """
    {
        "alt_id": "alt-999",
        "alt_type": "contact"
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("invoice_id", invoiceId)
                .put("status", "voided");

        when(invoiceService.voidInvoice(eq(invoiceId), eq(altId), eq(altType), any(InvoiceVoidRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/invoices/{invoice-id}/void", invoiceId)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .contentType("application/json")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"invoice_id\":\"inv-321\",\"status\":\"voided\"}"));

        verify(invoiceService, times(1)).voidInvoice(eq(invoiceId), eq(altId), eq(altType), any(InvoiceVoidRequest.class));
    }

    @Test
    void testSendInvoice() throws Exception {
        String invoiceId = "inv-777";
        String altId = "alt-777";
        String altType = "contact";

        String requestJson = """
    {
        "alt_id": "alt-777",
        "alt_type": "contact",
        "user_id": "user-001",
        "action": "send",
        "live_mode": true,
        "sent_from": {
            "from_name": "Admin",
            "from_email": "admin@example.com"
        },
        "auto_payment": {
            "enable": true,
            "type": "card",
            "payment_method_id": "pm_123",
            "customer_id": "cus_456",
            "card": {
                "brand": "visa",
                "last4": "4242"
            }
        }
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("invoice_id", invoiceId)
                .put("status", "sent");

        when(invoiceService.sendInvoice(eq(invoiceId), eq(altId), eq(altType), any(InvoiceSendRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/invoices/{invoice-id}/send", invoiceId)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .contentType("application/json")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"invoice_id\":\"inv-777\",\"status\":\"sent\"}"));

        verify(invoiceService, times(1)).sendInvoice(eq(invoiceId), eq(altId), eq(altType), any(InvoiceSendRequest.class));
    }

    @Test
    void testRecordPayment() throws Exception {
        String invoiceId = "inv-123";
        String altId = "alt-456";
        String altType = "contact";

        String requestJson = """
    {
        "alt_id": "alt-456",
        "alt_type": "contact",
        "mode": "card",
        "card": {
            "brand": "visa",
            "last4": "4242"
        },
        "notes": "Partial payment recorded.",
        "amount": 150.00,
        "payment_schedule_ids": ["ps1", "ps2"]
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("invoice_id", invoiceId)
                .put("payment_status", "recorded");

        when(invoiceService.recordPayment(eq(invoiceId), eq(altId), eq(altType), any(RecordPaymentRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/invoices/{invoice-id}/record-payment", invoiceId)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .contentType("application/json")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"invoice_id\":\"inv-123\",\"payment_status\":\"recorded\"}"));

        verify(invoiceService, times(1)).recordPayment(eq(invoiceId), eq(altId), eq(altType), any(RecordPaymentRequest.class));
    }

    @Test
    void testUpdateInvoiceLastVisitedAt() throws Exception {
        String requestJson = """
    {
        "invoice_id": "inv-999"
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("invoice_id", "inv-999")
                .put("last_visited_updated", true);

        when(invoiceService.updateInvoiceLastVisitedAt(any(InvoiceUpdateLastVisitedAtRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(patch("/api/v1/invoices/stats/last-visited-at")
                        .contentType("application/json")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "invoice_id": "inv-999",
                    "last_visited_updated": true
                }
            """));

        verify(invoiceService, times(1)).updateInvoiceLastVisitedAt(any(InvoiceUpdateLastVisitedAtRequest.class));
    }

    @Test
    void testCreateInvoice() throws Exception {
        String requestJson = """
    {
        "alt_id": "alt-001",
        "alt_type": "contact",
        "name": "Invoice for Client A",
        "currency": "USD",
        "invoice_number": "INV-1001",
        "issue_date": "2025-10-10",
        "due_date": "2025-10-17",
        "live_mode": true,
        "automatic_taxes_enabled": false
    }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("invoice_id", "inv-1001")
                .put("status", "created");

        when(invoiceService.createInvoice(any(InvoiceCreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/invoices")
                        .contentType("application/json")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                {
                    "invoice_id": "inv-1001",
                    "status": "created"
                }
            """));

        verify(invoiceService, times(1)).createInvoice(any(InvoiceCreateRequest.class));
    }

    @Test
    void testListInvoices() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("total", 2);

        String queryParam = "limit=10&skip=0";

        when(invoiceService.listInvoices(anyMap())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1")
                        .param("limit", "10")
                        .param("skip", "0")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":2}"));

        verify(invoiceService, times(1)).listInvoices(anyMap());
    }

}
