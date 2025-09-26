package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.estimate.*;

public interface EstimateService {
    JsonNode createEstimate(EstimateCreateRequest request);
    JsonNode updateEstimate(String estimateId, EstimateUpdateRequest request);
    JsonNode deleteEstimate(String estimateId);
    JsonNode generateEstimateNumber(String altId, String altType);
    JsonNode sendEstimate(String estimateId, EstimateSendRequest request);
    JsonNode createInvoiceFromEstimate(String estimateId, InvoiceCreateRequest request);
    JsonNode listEstimates(String altId, String altType, String limit, String offset,
                           String contactId, String endAt, String search, String startAt, String status);
    JsonNode updateEstimateLastVisitedAt(LastVisitedUpdateRequest request);
    JsonNode listEstimateTemplates(String altId, String altType, String limit, String offset, String search);
    JsonNode createEstimateTemplate(EstimateTemplateCreateRequest request);
    JsonNode updateEstimateTemplate(EstimateTemplateUpdateRequest request, String templateId);
    JsonNode deleteEstimateTemplate(String templateId, String altId, String altType);
    JsonNode previewEstimateTemplate(String altId, String altType, String templateId);
}
