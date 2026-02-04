package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.service.CategoryService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @AppKey
    @GetMapping("/category/{location-id}")
    public ResponseEntity<?> getCategories(
        @PathVariable("location-id") String locationId,
        @RequestParam(value = "limit", required = false) String limit,
        @RequestParam(value = "skip", required = false) String skip,
        @RequestParam(value = "search-text", required = false) String searchText
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "searchText", searchText);

        return ResponseEntity.ok(categoryService.getCategories(locationId, queryParams));
    }

    @AppKey
    @GetMapping("/category/{location-id}/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String categoryId
    ) {
        return ResponseEntity.ok(categoryService.getCategoryById(locationId, categoryId));
    }

}
