package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.review.GoHighLevelReviewBulkUpdateRequest;
import org.example.loficonnect.dto.mapper.review.GoHighLevelReviewUpdateRequest;
import org.example.loficonnect.dto.request.review.ReviewBulkUpdateRequest;
import org.example.loficonnect.dto.request.review.ReviewUpdateRequest;
import org.example.loficonnect.feignclients.ReviewClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ReviewService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewClient reviewClient;
    private final AuthorizationService authorizationService;

    public ReviewServiceImpl(ReviewClient reviewClient, AuthorizationService authorizationService) {
        this.reviewClient = reviewClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getReviews(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return reviewClient.getReviews(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getReviewCount(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return reviewClient.getReviewCount(accessKey, version, queryParams);
    }

    @Override
    public JsonNode updateReview(String reviewId, ReviewUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelReviewUpdateRequest ghlRequest = GoHighLevelReviewUpdateRequest.fromRequest(request);
        return reviewClient.updateReview(accessKey, version, reviewId, ghlRequest);
    }

    @Override
    public JsonNode deleteReview(String reviewId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return reviewClient.deleteReview(accessKey, version, reviewId, queryParams);
    }

    @Override
    public JsonNode bulkUpdateReviews(ReviewBulkUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelReviewBulkUpdateRequest ghlRequest = GoHighLevelReviewBulkUpdateRequest.fromRequest(request);
        return reviewClient.bulkUpdateReviews(accessKey, version, ghlRequest);
    }

}
