package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.review.ReviewBulkUpdateRequest;
import org.example.loficonnect.dto.request.review.ReviewUpdateRequest;
import org.example.loficonnect.service.ReviewService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @AppKey
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "end-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "0") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "product-id", required = false) String productId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "sort-field", required = false) String sortField,
            @RequestParam(value = "sort-order", required = false) String sortOrder,
            @RequestParam(value = "start-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam(value = "store-id", required = false) String storeId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "endDate", endDate);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "productId", productId);
        MapUtil.putIfNotNull(queryParams, "rating", rating);
        MapUtil.putIfNotNull(queryParams, "sortField", sortField);
        MapUtil.putIfNotNull(queryParams, "sortOrder", sortOrder);
        MapUtil.putIfNotNull(queryParams, "startDate", startDate);
        MapUtil.putIfNotNull(queryParams, "storeId", storeId);

        return ResponseEntity.ok(reviewService.getReviews(queryParams));
    }

    @AppKey
    @GetMapping("/reviews/count")
    public ResponseEntity<?> getReviewCount(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "end-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
            @RequestParam(value = "product-id", required = false) String productId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "start-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam(value = "store-id", required = false) String storeId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "endDate", endDate);
        MapUtil.putIfNotNull(queryParams, "productId", productId);
        MapUtil.putIfNotNull(queryParams, "rating", rating);
        MapUtil.putIfNotNull(queryParams, "startDate", startDate);
        MapUtil.putIfNotNull(queryParams, "storeId", storeId);

        return ResponseEntity.ok(reviewService.getReviewCount(queryParams));
    }

    @AppKey
    @PutMapping("/reviews/{review-id}")
    public ResponseEntity<?> updateReview(
            @PathVariable("review-id") String reviewId,
            @RequestBody ReviewUpdateRequest request
    ) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request));
    }

    @AppKey
    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable("review-id") String reviewId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("product-id") String productId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "productId", productId);

        return ResponseEntity.ok(reviewService.deleteReview(reviewId, queryParams));
    }

    @AppKey
    @PostMapping("/reviews/bulk-update")
    public ResponseEntity<?> bulkUpdateReviews(@RequestBody ReviewBulkUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewService.bulkUpdateReviews(request));
    }

}
