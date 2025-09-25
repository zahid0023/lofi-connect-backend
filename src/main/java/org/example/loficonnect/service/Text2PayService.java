package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.text2pay.Text2PayCreateRequest;

public interface Text2PayService {
    JsonNode createText2PayInvoice(Text2PayCreateRequest request);
}
