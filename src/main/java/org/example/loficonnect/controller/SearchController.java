package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;
import org.example.loficonnect.dto.request.search.TaskSearchRequest;
import org.example.loficonnect.service.SearchService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @AppKey
    @PostMapping("/contacts/search")
    public ResponseEntity<?> searchContacts(@RequestBody ContactSearchRequest request) {
        return ResponseEntity.ok(searchService.searchContacts(request));
    }

    @AppKey
    @GetMapping("/contacts/search/duplicate")
    public ResponseEntity<?> getDuplicateContacts(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "number", required = false) String number) {

        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "email", email);
        MapUtil.putIfNotNull(queryParams, "number", number);

        return ResponseEntity.ok(searchService.getDuplicateContacts(queryParams));
    }

    @AppKey
    @GetMapping("/locations/search")
    public ResponseEntity<?> searchLocations(
            @RequestParam(value = "company-id", required = false) String companyId,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "limit", required = false, defaultValue = "10") String limit,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "skip", required = false, defaultValue = "0") String skip
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "companyId", companyId);
        MapUtil.putIfNotNull(queryParams, "email", email);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "order", order);
        MapUtil.putIfNotNull(queryParams, "skip", skip);

        return ResponseEntity.ok(searchService.searchLocations(queryParams));
    }

    @AppKey
    @PostMapping("/locations/tasks/search")
    public ResponseEntity<?> searchTasks(
            @RequestBody TaskSearchRequest request
    ) {
        return ResponseEntity.ok(searchService.searchTasks(request));
    }

    @AppKey
    @GetMapping("/conversations/search")
    public ResponseEntity<?> searchConversations(
            @RequestParam(value = "assigned-to", required = false) String assignedTo,
            @RequestParam(value = "contact-id", required = false) String contactId,
            @RequestParam(value = "followers", required = false) String followers,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "last-message-action", required = false) String lastMessageAction,
            @RequestParam(value = "last-message-direction", required = false) String lastMessageDirection,
            @RequestParam(value = "last-message-type", required = false) String lastMessageType,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "mentions", required = false) String mentions,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "score-profile", required = false) String scoreProfile,
            @RequestParam(value = "score-profile-max", required = false) Integer scoreProfileMax,
            @RequestParam(value = "score-profile-min", required = false) Integer scoreProfileMin,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-score-profile", required = false) String sortScoreProfile,
            @RequestParam(value = "start-after-date", required = false) String startAfterDate,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "assignedTo", assignedTo);
        MapUtil.putIfNotNull(queryParams, "contactId", contactId);
        MapUtil.putIfNotNull(queryParams, "followers", followers);
        MapUtil.putIfNotNull(queryParams, "id", id);
        MapUtil.putIfNotNull(queryParams, "lastMessageAction", lastMessageAction);
        MapUtil.putIfNotNull(queryParams, "lastMessageDirection", lastMessageDirection);
        MapUtil.putIfNotNull(queryParams, "lastMessageType", lastMessageType);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "mentions", mentions);
        MapUtil.putIfNotNull(queryParams, "query", query);
        MapUtil.putIfNotNull(queryParams, "scoreProfile", scoreProfile);
        MapUtil.putIfNotNull(queryParams, "scoreProfileMax", scoreProfileMax);
        MapUtil.putIfNotNull(queryParams, "scoreProfileMin", scoreProfileMin);
        MapUtil.putIfNotNull(queryParams, "sort", sort);
        MapUtil.putIfNotNull(queryParams, "sortBy", sortBy);
        MapUtil.putIfNotNull(queryParams, "sortScoreProfile", sortScoreProfile);
        MapUtil.putIfNotNull(queryParams, "startAfterDate", startAfterDate);
        MapUtil.putIfNotNull(queryParams, "status", status);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(searchService.searchConversations(queryParams));
    }

    @AppKey
    @GetMapping("/opportunities/search")
    public ResponseEntity<?> searchOpportunities(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "pipeline-id", required = false) String pipelineId,
            @RequestParam(value = "pipeline-stage-id", required = false) String pipelineStageId,
            @RequestParam(value = "contact-id", required = false) String contactId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "assigned-to", required = false) String assignedTo,
            @RequestParam(value = "campaign-id", required = false) String campaignId,
            @RequestParam(value = "opportunity-id", required = false) String id,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "end-date", required = false) String endDate,
            @RequestParam(value = "start-after", required = false) String startAfter,
            @RequestParam(value = "start-after-id", required = false) String startAfterId,
            @RequestParam(value = "start-date", required = false) String date,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "get-tasks", required = false) Boolean getTasks,
            @RequestParam(value = "get-notes", required = false) Boolean getNotes,
            @RequestParam(value = "get-calendar-events", required = false) Boolean getCalendarEvents
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "q", q);
        MapUtil.putIfNotNull(queryParams, "pipeline_id", pipelineId);
        MapUtil.putIfNotNull(queryParams, "pipeline_stage_id", pipelineStageId);
        MapUtil.putIfNotNull(queryParams, "contact_id", contactId);
        MapUtil.putIfNotNull(queryParams, "status", status);
        MapUtil.putIfNotNull(queryParams, "assigned_to", assignedTo);
        MapUtil.putIfNotNull(queryParams, "campaignId", campaignId);
        MapUtil.putIfNotNull(queryParams, "id", id);
        MapUtil.putIfNotNull(queryParams, "order", order);
        MapUtil.putIfNotNull(queryParams, "endDate", endDate);
        MapUtil.putIfNotNull(queryParams, "startAfter", startAfter);
        MapUtil.putIfNotNull(queryParams, "startAfterId", startAfterId);
        MapUtil.putIfNotNull(queryParams, "date", date);
        MapUtil.putIfNotNull(queryParams, "country", country);
        MapUtil.putIfNotNull(queryParams, "page", page);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "getTasks", getTasks);
        MapUtil.putIfNotNull(queryParams, "getNotes", getNotes);
        MapUtil.putIfNotNull(queryParams, "getCalendarEvents", getCalendarEvents);

        return ResponseEntity.ok(searchService.searchOpportunities(queryParams));
    }
}
