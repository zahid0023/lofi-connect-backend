package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.service.TemplateService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class TemplateController {
    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @AppKey
    @GetMapping("/templates")
    public ResponseEntity<?> getTemplates(
            @RequestParam("location-id") String locationId,
            @RequestParam(value = "deleted", required = false, defaultValue = "false") Boolean deleted,
            @RequestParam(value = "limit", required = false, defaultValue = "25") String limit,
            @RequestParam(value = "skip", required = false, defaultValue = "0") String skip,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "origin-id") String originId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "deleted", deleted);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "type", type);
        MapUtil.putIfNotNull(queryParams, "originId", originId);

        return ResponseEntity.ok(templateService.getTemplates(locationId, queryParams));
    }

    @AppKey
    @DeleteMapping("/templates/{id}")
    public ResponseEntity<?> deleteTemplate(
            @RequestParam("location-id") String locationId,
            @PathVariable("id") String templateId
    ) {
        return ResponseEntity.ok(templateService.deleteTemplate(locationId, templateId));
    }


}
