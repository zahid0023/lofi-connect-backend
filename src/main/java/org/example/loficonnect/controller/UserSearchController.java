package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.usersearch.UserSearchRequest;
import org.example.loficonnect.service.UserSearchService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class UserSearchController {

    private final UserSearchService userSearchService;

    public UserSearchController(UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    @AppKey
    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam("company-id") String companyId,
            @RequestParam(value = "enabled2waySync", required = false) Boolean enabled2waySync,
            @RequestParam(value = "ids", required = false) String ids,
            @RequestParam(value = "limit", required = false) String limit,
            @RequestParam(value = "skip", required = false) String skip,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "sort-direction", required = false) String sortDirection,
            @RequestParam(value = "type", required = false) String type
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "companyId", companyId);
        MapUtil.putIfNotNull(queryParams, "enabled2waySync", enabled2waySync);
        MapUtil.putIfNotNull(queryParams, "ids", ids);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "query", query);
        MapUtil.putIfNotNull(queryParams, "role", role);
        MapUtil.putIfNotNull(queryParams, "sort", sort);
        MapUtil.putIfNotNull(queryParams, "sortDirection", sortDirection);
        MapUtil.putIfNotNull(queryParams, "type", type);

        return ResponseEntity.ok(userSearchService.searchUsers(queryParams));
    }

    @AppKey
    @PostMapping("/users/search/filter-by-email")
    public ResponseEntity<?> filterUsersByEmail(@RequestBody UserSearchRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userSearchService.filterUsersByEmail(request));
    }
}
