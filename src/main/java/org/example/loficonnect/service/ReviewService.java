package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.review.ReviewBulkUpdateRequest;
import org.example.loficonnect.dto.request.review.ReviewUpdateRequest;

import java.util.Map;

public interface ReviewService {
    JsonNode getReviews(Map<String, Object> queryParams);
    JsonNode getReviewCount(Map<String, Object> queryParams);
    JsonNode updateReview(String reviewId, ReviewUpdateRequest request);
    JsonNode deleteReview(String reviewId, Map<String, Object> queryParams);
    JsonNode bulkUpdateReviews(ReviewBulkUpdateRequest request);
}
