package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelInvoiceTemplateCreateRequest;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelInvoiceTemplateUpdateRequest;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelLateFeesConfigurationUpdateRequest;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelPaymentMethodsConfigurationUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "invoiceTemplateClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface InvoiceTemplateClient {

    @PostMapping(value = "/invoices/template", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode createTemplate(@RequestHeader("Authorization") String authorization,
                            @RequestHeader("Version") String version,
                            @RequestBody GoHighLevelInvoiceTemplateCreateRequest request);

    @GetMapping(value = "/invoices/template", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode listTemplates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams);

    @GetMapping(value = "/invoices/template/{templateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode getTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestParam Map<String, Object> queryParams);

    @PutMapping(value = "/invoices/template/{templateId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode updateTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestBody GoHighLevelInvoiceTemplateUpdateRequest request);

    @DeleteMapping(value = "/invoices/template/{templateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode deleteTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestParam Map<String, Object> queryParams);

    @PatchMapping(value = "/invoices/template/{templateId}/late-fees-configuration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode updateLateFeesConfiguration(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestBody GoHighLevelLateFeesConfigurationUpdateRequest request);

    @PatchMapping(value = "/invoices/template/{templateId}/payment-methods-configuration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode updatePaymentMethodsConfiguration(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestBody GoHighLevelPaymentMethodsConfigurationUpdateRequest request);
}

