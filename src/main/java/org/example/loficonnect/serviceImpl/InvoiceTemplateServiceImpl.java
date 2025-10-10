package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelInvoiceTemplateCreateRequest;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelInvoiceTemplateUpdateRequest;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelLateFeesConfigurationUpdateRequest;
import org.example.loficonnect.dto.mapper.invoicetemplate.GoHighLevelPaymentMethodsConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateCreateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateLateFeesConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplatePaymentMethodsConfigurationUpdateRequest;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateUpdateRequest;
import org.example.loficonnect.feignclients.InvoiceTemplateClient;
import org.example.loficonnect.service.InvoiceTemplateService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class InvoiceTemplateServiceImpl implements InvoiceTemplateService {

    private final AuthorizationServiceImpl authorizationService;
    private final InvoiceTemplateClient invoiceTemplateClient;

    public InvoiceTemplateServiceImpl(AuthorizationServiceImpl authorizationService, InvoiceTemplateClient invoiceTemplateClient) {
        this.authorizationService = authorizationService;
        this.invoiceTemplateClient = invoiceTemplateClient;
    }
    
    @Override
    public JsonNode createInvoiceTemplate(InvoiceTemplateCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelInvoiceTemplateCreateRequest ghlRequest = GoHighLevelInvoiceTemplateCreateRequest.fromRequest(request);
        
        return invoiceTemplateClient.createTemplate(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode listInvoiceTemplates(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoiceTemplateClient.listTemplates(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getInvoiceTemplate(String templateId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoiceTemplateClient.getTemplate(accessKey, version, templateId, queryParams);
    }

    @Override
    public JsonNode updateInvoiceTemplate(String templateId, InvoiceTemplateUpdateRequest request) {
        GoHighLevelInvoiceTemplateUpdateRequest ghlRequest = GoHighLevelInvoiceTemplateUpdateRequest.fromRequest(request);
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoiceTemplateClient.updateTemplate(accessKey, version, templateId, ghlRequest);
    }

    @Override
    public JsonNode deleteInvoiceTemplate(String templateId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoiceTemplateClient.deleteTemplate(accessKey, version, templateId, queryParams);
    }

    @Override
    public JsonNode updateLateFeesConfiguration(String templateId, InvoiceTemplateLateFeesConfigurationUpdateRequest request) {
        GoHighLevelLateFeesConfigurationUpdateRequest ghlRequest = GoHighLevelLateFeesConfigurationUpdateRequest.fromRequest(request);
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoiceTemplateClient.updateLateFeesConfiguration(accessKey, version, templateId, ghlRequest);
    }

    @Override
    public JsonNode updatePaymentMethodsConfiguration(String templateId, InvoiceTemplatePaymentMethodsConfigurationUpdateRequest request) {
        GoHighLevelPaymentMethodsConfigurationUpdateRequest ghlRequest = GoHighLevelPaymentMethodsConfigurationUpdateRequest.fromRequest(request);
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoiceTemplateClient.updatePaymentMethodsConfiguration(accessKey, version, templateId, ghlRequest);
    }
}
