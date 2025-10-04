package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.documents.DocumentSendRequest;
import org.example.loficonnect.service.DocumentService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getDocuments(
            @RequestParam("location-id") String locationId,
            @RequestParam(value = "date-from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateFrom,
            @RequestParam(value = "date-to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateTo,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "payment-status", required = false) String paymentStatus,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "skip", required = false) Integer skip,
            @RequestParam(value = "status", required = false) String status
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "dateFrom", dateFrom);
        MapUtil.putIfNotNull(queryParams, "dateTo", dateTo);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "paymentStatus", paymentStatus);
        MapUtil.putIfNotNull(queryParams, "query", query);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "status", status);

        return ResponseEntity.ok(documentService.getDocuments(queryParams));
    }

    @AppKey
    @PostMapping("/send")
    public ResponseEntity<?> sendDocument(@RequestBody DocumentSendRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(documentService.sendDocument(request));
    }

}
