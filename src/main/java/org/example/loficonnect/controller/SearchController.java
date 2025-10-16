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
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "locationId") String locationId) {

        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "email", email);
        MapUtil.putIfNotNull(queryParams, "number", number);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(searchService.getDuplicateContacts(queryParams));
    }

    @AppKey
    @GetMapping("/locations/search")
    public ResponseEntity<?> searchLocations(
            @RequestParam(value = "companyId", required = false) String companyId,
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
    @PostMapping("/locations/{location-id}/tasks/search")
    public ResponseEntity<?> searchTasks(
            @PathVariable("location-id") String locationId,
            @RequestBody TaskSearchRequest request
    ) {
        return ResponseEntity.ok(searchService.searchTasks(locationId, request));
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


}
