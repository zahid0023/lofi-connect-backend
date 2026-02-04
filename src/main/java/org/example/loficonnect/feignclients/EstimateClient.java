package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.estimate.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "estimateClient", url = "https://services.leadconnectorhq.com",configuration = FeignLoggingConfig.class)
public interface EstimateClient {

    @PostMapping(value = "/invoices/estimate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode createEstimate(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelEstimateCreateRequest request
    );

    @PutMapping(value = "/invoices/estimate/{estimateId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode updateEstimate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("estimateId") String estimateId,
            @RequestBody GoHighLevelEstimateUpdateRequest request
    );

    @DeleteMapping(value = "/invoices/estimate/{estimateId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode deleteEstimate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("estimateId") String estimateId
    );

    @GetMapping(value = "/invoices/estimate/number/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode generateEstimateNumber(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @PostMapping(value = "/invoices/estimate/{estimateId}/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode sendEstimate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("estimateId") String estimateId,
            @RequestBody GoHighLevelEstimateSendRequest request
    );

    @PostMapping(value = "/invoices/estimate/{estimateId}/invoice", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode createInvoiceFromEstimate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("estimateId") String estimateId,
            @RequestBody GoHighLevelInvoiceCreateRequest request
    );

    @GetMapping(value = "/invoices/estimate/list", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode listEstimates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestParam(value = "contactId", required = false) String contactId,
            @RequestParam(value = "endAt", required = false) String endAt,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "startAt", required = false) String startAt,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset
    );

    @PatchMapping(value = "/invoices/estimate/stats/last-visited-at", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode updateEstimateLastVisitedAt(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelLastVisitedUpdateRequest request
    );

    @GetMapping(value = "/invoices/estimate/template", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode listEstimateTemplates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset,
            @RequestParam(value = "search", required = false) String search
    );

    @PostMapping(value = "/invoices/estimate/template", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode createEstimateTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelEstimateTemplateCreateRequest request
    );

    @PostMapping(
            value = "/invoices/estimate/template/{templateId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateEstimateTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestBody GoHighLevelEstimateTemplateUpdateRequest request
    );

    @DeleteMapping(
            value = "/invoices/estimate/template/{templateId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteEstimateTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("templateId") String templateId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @GetMapping(
            value = "/invoices/estimate/template/preview",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode previewEstimateTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestParam("templateId") String templateId
    );
}
