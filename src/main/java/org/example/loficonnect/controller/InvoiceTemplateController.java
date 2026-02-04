package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateCreateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateLateFeesConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplatePaymentMethodsConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateUpdateRequest;
import org.example.loficonnect.service.InvoiceTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class InvoiceTemplateController {

    private final InvoiceTemplateService invoiceTemplateService;

    public InvoiceTemplateController(InvoiceTemplateService invoiceTemplateService) {
        this.invoiceTemplateService = invoiceTemplateService;
    }

    @AppKey
    @PostMapping("/invoices/template")
    public ResponseEntity<?> createInvoiceTemplate(@RequestBody InvoiceTemplateCreateRequest request) {
        JsonNode response = invoiceTemplateService.createInvoiceTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AppKey
    @GetMapping("/invoices/template")
    public ResponseEntity<?> listInvoiceTemplates(
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType,
            @RequestParam(value = "limit", defaultValue = "10") String limit,
            @RequestParam(value = "offset", defaultValue = "0") String offset,
            @RequestParam(value = "start-at", required = false) String startAt,
            @RequestParam(value = "end-at", required = false) String endAt,
            @RequestParam(value = "payment-mode", required = false) String paymentMode,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "search", required = false) String search) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("altId", altId);
        queryParams.put("altType", altType);
        queryParams.put("limit", limit);
        queryParams.put("offset", offset);
        if (startAt != null) queryParams.put("startAt", startAt);
        if (endAt != null) queryParams.put("endAt", endAt);
        if (paymentMode != null) queryParams.put("paymentMode", paymentMode);
        if (status != null) queryParams.put("status", status);
        if (search != null) queryParams.put("search", search);

        JsonNode response = invoiceTemplateService.listInvoiceTemplates(queryParams);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @GetMapping("/invoices/template/{template-id}")
    public ResponseEntity<?> getInvoiceTemplate(
            @PathVariable("template-id") String templateId,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("altId", altId);
        queryParams.put("altType", altType);

        JsonNode response = invoiceTemplateService.getInvoiceTemplate(templateId, queryParams);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PutMapping("/invoices/template/{template-id}")
    public ResponseEntity<?> updateInvoiceTemplate(
            @PathVariable("template-id") String templateId,
            @RequestBody InvoiceTemplateUpdateRequest request) {

        JsonNode response = invoiceTemplateService.updateInvoiceTemplate(templateId, request);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @DeleteMapping("/invoices/template/{template-id}")
    public ResponseEntity<?> deleteInvoiceTemplate(
            @PathVariable("template-id") String templateId,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("altId", altId);
        queryParams.put("altType", altType);

        JsonNode response = invoiceTemplateService.deleteInvoiceTemplate(templateId, queryParams);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @AppKey
    @PatchMapping("/invoices/template/{template-id}/late-fees-configuration")
    public ResponseEntity<?> updateLateFeesConfiguration(
            @PathVariable("template-id") String templateId,
            @RequestBody InvoiceTemplateLateFeesConfigurationUpdateRequest request) {

        JsonNode response = invoiceTemplateService.updateLateFeesConfiguration(templateId, request);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PatchMapping("/invoices/template/{template-id}/payment-methods-configuration")
    public ResponseEntity<?> updatePaymentMethodsConfiguration(
            @PathVariable("template-id") String templateId,
            @RequestBody InvoiceTemplatePaymentMethodsConfigurationUpdateRequest request) {

        JsonNode response = invoiceTemplateService.updatePaymentMethodsConfiguration(templateId, request);
        return ResponseEntity.ok(response);
    }
}
