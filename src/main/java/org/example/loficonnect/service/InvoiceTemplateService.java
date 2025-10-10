package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateCreateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateLateFeesConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplatePaymentMethodsConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateUpdateRequest;

import java.util.Map;

public interface InvoiceTemplateService {
    JsonNode createInvoiceTemplate(InvoiceTemplateCreateRequest request);
    JsonNode listInvoiceTemplates(Map<String, Object> queryParams);
    JsonNode getInvoiceTemplate(String templateId, Map<String, Object> queryParams);
    JsonNode updateInvoiceTemplate(String templateId, InvoiceTemplateUpdateRequest request);
    JsonNode deleteInvoiceTemplate(String templateId, Map<String, Object> queryParams);
    JsonNode updateLateFeesConfiguration(String templateId, InvoiceTemplateLateFeesConfigurationUpdateRequest request);
    JsonNode updatePaymentMethodsConfiguration(String templateId, InvoiceTemplatePaymentMethodsConfigurationUpdateRequest request);
}

