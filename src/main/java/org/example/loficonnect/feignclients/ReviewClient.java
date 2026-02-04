package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.review.GoHighLevelReviewBulkUpdateRequest;
import org.example.loficonnect.dto.mapper.review.GoHighLevelReviewUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "reviewClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ReviewClient {

    @GetMapping(
            value = "/products/reviews",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getReviews(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/products/reviews/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getReviewCount(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/products/reviews/{reviewId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateReview(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("reviewId") String reviewId,
            @RequestBody GoHighLevelReviewUpdateRequest request
    );

    @DeleteMapping(
            value = "/products/reviews/{reviewId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteReview(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("reviewId") String reviewId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/products/reviews/bulk-update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode bulkUpdateReviews(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelReviewBulkUpdateRequest request
    );

}
