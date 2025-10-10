package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.documentstemplate.TemplateSendRequest;
import org.example.loficonnect.service.DocumentsTemplateService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents/templates")
public class DocumentsTemplateController {

    private final DocumentsTemplateService documentsTemplateService;

    public DocumentsTemplateController(DocumentsTemplateService documentsTemplateService) {
        this.documentsTemplateService = documentsTemplateService;
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getTemplates(
            @RequestParam("location-id") String locationId,
            @RequestParam(value = "date-from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateFrom,
            @RequestParam(value = "date-to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateTo,
            @RequestParam(value = "is-public-document", required = false) Boolean isPublicDocument,
            @RequestParam(value = "limit", required = false) String limit,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "skip", required = false) String skip,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "user-id", required = false) String userId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "dateFrom", dateFrom);
        MapUtil.putIfNotNull(queryParams, "dateTo", dateTo);
        MapUtil.putIfNotNull(queryParams, "isPublicDocument", isPublicDocument);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "name", name);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "type", type);
        MapUtil.putIfNotNull(queryParams, "userId", userId);

        return ResponseEntity.ok(documentsTemplateService.getTemplates(queryParams));
    }


    @AppKey
    @PostMapping("/send")
    public ResponseEntity<?> sendTemplate(@RequestBody TemplateSendRequest request) {
        return ResponseEntity.ok(documentsTemplateService.sendTemplate(request));
    }

}
