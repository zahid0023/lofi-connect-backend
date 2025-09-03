package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;
import org.example.loficonnect.service.SearchService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
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


}
