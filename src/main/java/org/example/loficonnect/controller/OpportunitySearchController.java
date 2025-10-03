package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.OpportunitySearchService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/opportunities/search")
public class OpportunitySearchController {

    private final OpportunitySearchService opportunitySearchService;

    public OpportunitySearchController(OpportunitySearchService opportunitySearchService) {
        this.opportunitySearchService = opportunitySearchService;
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> searchOpportunities(
        @RequestParam("location-id") String locationId,
        @RequestParam(value = "assigned-to", required = false) String assignedTo,
        @RequestParam(value = "campaign-id", required = false) String campaignId,
        @RequestParam(value = "contact-id", required = false) String contactId,
        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "date", required = false) String date,
        @RequestParam(value = "end-date", required = false) String endDate,
        @RequestParam(value = "getCalendarEvents", required = false) Boolean getCalendarEvents,
        @RequestParam(value = "getNotes", required = false) Boolean getNotes,
        @RequestParam(value = "getTasks", required = false) Boolean getTasks,
        @RequestParam(value = "id", required = false) String id,
        @RequestParam(value = "limit", required = false) Integer limit,
        @RequestParam(value = "order", required = false) String order,
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "pipeline-id", required = false) String pipelineId,
        @RequestParam(value = "pipeline-stage-id", required = false) String pipelineStageId,
        @RequestParam(value = "q", required = false) String q,
        @RequestParam(value = "start-after", required = false) String startAfter,
        @RequestParam(value = "start-after-id", required = false) String startAfterId,
        @RequestParam(value = "status", required = false) String status
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "location-id", locationId);
        MapUtil.putIfNotNull(queryParams, "assigned-to", assignedTo);
        MapUtil.putIfNotNull(queryParams, "campaign-id", campaignId);
        MapUtil.putIfNotNull(queryParams, "contact-id", contactId);
        MapUtil.putIfNotNull(queryParams, "country", country);
        MapUtil.putIfNotNull(queryParams, "date", date);
        MapUtil.putIfNotNull(queryParams, "endDate", endDate);
        MapUtil.putIfNotNull(queryParams, "getCalendarEvents", getCalendarEvents);
        MapUtil.putIfNotNull(queryParams, "getNotes", getNotes);
        MapUtil.putIfNotNull(queryParams, "getTasks", getTasks);
        MapUtil.putIfNotNull(queryParams, "id", id);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "order", order);
        MapUtil.putIfNotNull(queryParams, "page", page);
        MapUtil.putIfNotNull(queryParams, "pipeline-id", pipelineId);
        MapUtil.putIfNotNull(queryParams, "pipeline-stage-id", pipelineStageId);
        MapUtil.putIfNotNull(queryParams, "q", q);
        MapUtil.putIfNotNull(queryParams, "start-after", startAfter);
        MapUtil.putIfNotNull(queryParams, "start-after-id", startAfterId);
        MapUtil.putIfNotNull(queryParams, "status", status);

        return ResponseEntity.ok(opportunitySearchService.searchOpportunities(queryParams));
    }
}
