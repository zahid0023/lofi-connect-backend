package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.text2pay.Text2PayCreateRequest;
import org.example.loficonnect.service.Text2PayService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class Text2PayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private Text2PayService text2PayService;

    @InjectMocks
    private Text2PayController text2PayController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(text2PayController).build();
    }

    @Test
    void testCreateText2PayInvoice() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "created");

        when(text2PayService.createText2PayInvoice(any(Text2PayCreateRequest.class))).thenReturn(mockResponse);

        String requestBody = """
        {
            "alt_id": "abc-123",
            "alt_type": "custom",
            "name": "Sample Invoice",
            "currency": "USD",
            "items": [],
            "terms_notes": "Thank you",
            "title": "Invoice Title",
            "contact_details": {
                "id": "contact-1",
                "name": "John Doe",
                "phone_no": "1234567890",
                "email": "john@example.com",
                "additional_emails": [],
                "company_name": "Acme Corp",
                "address": {
                    "address_line1": "123 Street",
                    "address_line2": "Apt 4",
                    "city": "City",
                    "state": "State",
                    "country_code": "US",
                    "postal_code": "12345"
                },
                "custom_fields": []
            },
            "invoice_number": "INV-001",
            "issue_date": "2025-10-01",
            "due_date": "2025-10-10",
            "sent_to": {
                "email": ["john@example.com"],
                "email_cc": [],
                "email_bcc": [],
                "phone_no": ["1234567890"]
            },
            "live_mode": true,
            "automatic_taxes_enabled": false,
            "payment_schedule": {
                "type": "one-time",
                "schedules": []
            },
            "late_fees_configuration": {
                "enable": true,
                "value": 5,
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
            },
            "tips_configuration": {
                "tips_percentage": [10, 15],
                "tips_enabled": true
            },
            "invoice_number_prefix": "INV-",
            "payment_methods": {
                "stripe": {
                    "enable_bank_debit_only": true
                }
            },
            "attachments": [],
            "id": "text2pay-001",
            "include_terms_note": true,
            "action": "send",
            "user_id": "user-123",
            "discount": {
                "value": 10,
                "type": "percentage",
                "valid_on_product_ids": []
            },
            "business_details": {
                "logo_url": "https://example.com/logo.png",
                "name": "Acme Corp",
                "phone_no": "1234567890",
                "address": "123 Business St",
                "website": "https://example.com",
                "custom_values": []
            }
        }
        """;

        mockMvc.perform(post("/api/v1/text2pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\": \"created\"}"));

        verify(text2PayService, times(1)).createText2PayInvoice(any(Text2PayCreateRequest.class));
    }
}
