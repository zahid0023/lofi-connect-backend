package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.invoice.*;
import org.example.loficonnect.dto.request.invoice.*;
import org.example.loficonnect.feignclients.InvoicesClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.InvoiceService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final AuthorizationService authorizationService;
    private final InvoicesClient invoicesClient;

    public InvoiceServiceImpl(AuthorizationService authorizationService, InvoicesClient invoicesClient) {
        this.authorizationService = authorizationService;
        this.invoicesClient = invoicesClient;
    }

    @Override
    public JsonNode generateInvoiceNumber(String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoicesClient.generateInvoiceNumber(accessKey, version, altId, altType);
    }

    @Override
    public JsonNode getInvoice(String invoiceId, String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoicesClient.getInvoice(accessKey, version, invoiceId, altId, altType);
    }

    @Override
    public JsonNode updateInvoice(String invoiceId, InvoiceUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelInvoiceUpdateRequest ghlRequest = GoHighLevelInvoiceUpdateRequest.fromRequest(request);
        return invoicesClient.updateInvoice(accessKey, version, invoiceId, ghlRequest);
    }

    @Override
    public JsonNode deleteInvoice(String invoiceId, String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoicesClient.deleteInvoice(accessKey, version, invoiceId, altId, altType);
    }

    @Override
    public JsonNode updateLateFeesConfiguration(String invoiceId, String altId, String altType, InvoiceLateFeesConfigurationRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoicesClient.updateLateFeesConfiguration(accessKey, version, invoiceId, altId, altType, request);
    }

    @Override
    public JsonNode voidInvoice(String invoiceId, String altId, String altType, InvoiceVoidRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelInvoiceVoidRequest ghlRequest = GoHighLevelInvoiceVoidRequest.fromRequest(request);

        return invoicesClient.voidInvoice(accessKey, version, invoiceId, altId, altType, ghlRequest);
    }

    @Override
    public JsonNode sendInvoice(String invoiceId, String altId, String altType, InvoiceSendRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelInvoiceSendRequest ghlRequest = GoHighLevelInvoiceSendRequest.fromRequest(request);

        return invoicesClient.sendInvoice(accessKey, version, invoiceId, altId, altType, ghlRequest);
    }

    @Override
    public JsonNode recordPayment(String invoiceId, String altId, String altType, RecordPaymentRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelRecordPaymentRequest ghlRequest = GoHighLevelRecordPaymentRequest.fromRequest(request);

        return invoicesClient.recordPayment(accessKey, version, invoiceId, altId, altType, ghlRequest);
    }

    @Override
    public JsonNode updateInvoiceLastVisitedAt(InvoiceUpdateLastVisitedAtRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelInvoiceUpdateLastVisitedAtRequest ghlRequest = GoHighLevelInvoiceUpdateLastVisitedAtRequest.fromRequest(request);

        return invoicesClient.updateInvoiceLastVisitedAt(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode createInvoice(InvoiceCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelInvoiceCreateRequest ghlRequest = GoHighLevelInvoiceCreateRequest.fromRequest(request);

        return invoicesClient.createInvoice(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode listInvoices(Map<String, String> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return invoicesClient.listInvoices(accessKey, version, queryParams);
    }
}
