package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.estimate.*;
import org.example.loficonnect.service.EstimateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EstimateController {

    private final EstimateService estimateService;

    public EstimateController(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createEstimate(@RequestBody EstimateCreateRequest request) {
        JsonNode response = estimateService.createEstimate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AppKey
    @PutMapping("/{estimateId}")
    public ResponseEntity<?> updateEstimate(
            @PathVariable String estimateId,
            @RequestBody EstimateUpdateRequest request
    ) {
        JsonNode response = estimateService.updateEstimate(estimateId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AppKey
    @DeleteMapping("/{estimateId}")
    public ResponseEntity<?> deleteEstimate(
            @PathVariable String estimateId
    ) {
        JsonNode response = estimateService.deleteEstimate(estimateId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @AppKey
    @GetMapping("/generate-number")
    public ResponseEntity<?> generateEstimateNumber(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode response = estimateService.generateEstimateNumber(altId, altType);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PostMapping("/{estimateId}/send")
    public ResponseEntity<?> sendEstimate(
            @PathVariable String estimateId,
            @RequestBody EstimateSendRequest request
    ) {
        JsonNode response = estimateService.sendEstimate(estimateId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AppKey
    @PostMapping("/{estimateId}/invoice")
    public ResponseEntity<?> createInvoiceFromEstimate(
            @PathVariable String estimateId,
            @RequestBody InvoiceCreateRequest request
    ) {
        JsonNode response = estimateService.createInvoiceFromEstimate(estimateId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AppKey
    @GetMapping("/list")
    public ResponseEntity<?> listEstimates(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset,
            @RequestParam(value = "contact-id", required = false) String contactId,
            @RequestParam(value = "end-at", required = false) String endAt,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start-at", required = false) String startAt,
            @RequestParam(value = "status", required = false) String status
    ) {
        JsonNode response = estimateService.listEstimates(altId, altType, limit, offset, contactId, endAt, search, startAt, status);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PatchMapping("/stats/last-visited-at")
    public ResponseEntity<?> updateEstimateLastVisitedAt(
            @RequestBody LastVisitedUpdateRequest request
    ) {
        JsonNode response = estimateService.updateEstimateLastVisitedAt(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AppKey
    @GetMapping("/templates")
    public ResponseEntity<?> listEstimateTemplates(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset,
            @RequestParam(value = "search", required = false) String search
    ) {
        JsonNode response = estimateService.listEstimateTemplates(altId, altType, limit, offset, search);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PostMapping("/template")
    public ResponseEntity<?> createEstimateTemplate(
            @RequestBody EstimateTemplateCreateRequest request
    ) {
        JsonNode response = estimateService.createEstimateTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AppKey
    @PutMapping("/template/{template-id}")
    public ResponseEntity<?> updateEstimateTemplate(
            @RequestBody EstimateTemplateUpdateRequest request,
            @PathVariable("template-id") String templateId
    ) {
        JsonNode response = estimateService.updateEstimateTemplate(request, templateId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AppKey
    @DeleteMapping("/template/{template-id}")
    public ResponseEntity<?> deleteEstimateTemplate(
            @PathVariable("template-id") String templateId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode response = estimateService.deleteEstimateTemplate(templateId, altId, altType);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AppKey
    @GetMapping("/template/preview")
    public ResponseEntity<?> previewEstimateTemplate(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("template-id") String templateId
    ) {
        JsonNode response = estimateService.previewEstimateTemplate(altId, altType, templateId);
        return ResponseEntity.ok(response);
    }
}
