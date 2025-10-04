package org.example.loficonnect.dto.mapper.review;

import lombok.Data;
import org.example.loficonnect.dto.request.review.ReviewBulkUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelReviewBulkUpdateRequest {
    private String altId;
    private String altType;
    private List<Review> reviews;
    private String status;

    @Data
    public static class Review {
        private String reviewId;
        private String productId;
        private String storeId;
    }

    public static GoHighLevelReviewBulkUpdateRequest fromRequest(ReviewBulkUpdateRequest request) {
        GoHighLevelReviewBulkUpdateRequest ghl = new GoHighLevelReviewBulkUpdateRequest();
        ghl.setAltId(request.getAltId());
        ghl.setAltType(request.getAltType());
        ghl.setStatus(request.getStatus());

        if (request.getReviews() != null) {
            List<Review> reviewList = request.getReviews().stream().map(r -> {
                Review review = new Review();
                review.setReviewId(r.getReviewId());
                review.setProductId(r.getProductId());
                review.setStoreId(r.getStoreId());
                return review;
            }).collect(Collectors.toList());
            ghl.setReviews(reviewList);
        }

        return ghl;
    }
}
