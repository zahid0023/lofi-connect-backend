package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.estimate.*;
import org.example.loficonnect.dto.request.estimate.*;
import org.example.loficonnect.feignclients.EstimateClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.EstimateService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EstimateServiceImpl implements EstimateService {

    private final EstimateClient estimateClient;
    private final AuthorizationService authorizationService;

    public EstimateServiceImpl(EstimateClient estimateClient, AuthorizationService authorizationService) {
        this.estimateClient = estimateClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createEstimate(EstimateCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEstimateCreateRequest ghlRequest = GoHighLevelEstimateCreateRequest.fromRequest(request);
        return estimateClient.createEstimate(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode updateEstimate(String estimateId, EstimateUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEstimateUpdateRequest ghlRequest = GoHighLevelEstimateUpdateRequest.fromRequest(request);
        return estimateClient.updateEstimate(accessKey, version, estimateId, ghlRequest);
    }

    @Override
    public JsonNode deleteEstimate(String estimateId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return estimateClient.deleteEstimate(accessKey, version, estimateId);
    }

    @Override
    public JsonNode generateEstimateNumber(String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return estimateClient.generateEstimateNumber(accessKey, version, altId, altType);
    }

    @Override
    public JsonNode sendEstimate(String estimateId, EstimateSendRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEstimateSendRequest ghlRequest = GoHighLevelEstimateSendRequest.fromRequest(request);
        return estimateClient.sendEstimate(accessKey, version, estimateId, ghlRequest);
    }

    @Override
    public JsonNode createInvoiceFromEstimate(String estimateId, InvoiceCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelInvoiceCreateRequest ghlRequest = GoHighLevelInvoiceCreateRequest.fromRequest(request);
        return estimateClient.createInvoiceFromEstimate(accessKey, version, estimateId, ghlRequest);
    }

    @Override
    public JsonNode listEstimates(String altId, String altType, String limit, String offset,
                                  String contactId, String endAt, String search, String startAt, String status) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return estimateClient.listEstimates(accessKey, version, altId, altType, contactId, endAt, search, startAt, status, limit, offset);
    }

    @Override
    public JsonNode updateEstimateLastVisitedAt(LastVisitedUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelLastVisitedUpdateRequest ghlRequest = GoHighLevelLastVisitedUpdateRequest.fromRequest(request);
        return estimateClient.updateEstimateLastVisitedAt(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode listEstimateTemplates(String altId, String altType, String limit, String offset, String search) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return estimateClient.listEstimateTemplates(accessKey, version, altId, altType, limit, offset, search);
    }

    @Override
    public JsonNode createEstimateTemplate(EstimateTemplateCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEstimateTemplateCreateRequest ghlRequest = GoHighLevelEstimateTemplateCreateRequest.fromRequest(request);
        return estimateClient.createEstimateTemplate(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode updateEstimateTemplate(EstimateTemplateUpdateRequest request, String templateId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEstimateTemplateUpdateRequest ghlRequest = GoHighLevelEstimateTemplateUpdateRequest.fromRequest(request);
        return estimateClient.updateEstimateTemplate(accessKey, version, templateId, ghlRequest);
    }

    @Override
    public JsonNode deleteEstimateTemplate(String templateId, String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return estimateClient.deleteEstimateTemplate(accessKey, version, templateId, altId, altType);
    }

    @Override
    public JsonNode previewEstimateTemplate(String altId, String altType, String templateId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return estimateClient.previewEstimateTemplate(accessKey, version, altId, altType, templateId);
    }
}
