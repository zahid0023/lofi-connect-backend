package org.example.loficonnect.dto.mapper.review;

import lombok.Data;
import org.example.loficonnect.dto.request.review.ReviewUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelReviewUpdateRequest {
    private String altId;
    private String altType;
    private String productId;
    private String status;
    private List<Reply> reply;
    private String rating;
    private String headline;
    private String detail;

    @Data
    public static class Reply {
        private String headline;
        private String comment;
        private User user;
    }

    @Data
    public static class User {
        private String name;
        private String email;
        private String phone;
        private Boolean isCustomer;
    }

    public static GoHighLevelReviewUpdateRequest fromRequest(ReviewUpdateRequest request) {
        GoHighLevelReviewUpdateRequest ghl = new GoHighLevelReviewUpdateRequest();
        ghl.setAltId(request.getAltId());
        ghl.setAltType(request.getAltType());
        ghl.setProductId(request.getProductId());
        ghl.setStatus(request.getStatus());
        ghl.setRating(request.getRating());
        ghl.setHeadline(request.getHeadline());
        ghl.setDetail(request.getDetail());

        if (request.getReply() != null) {
            List<Reply> replies = request.getReply().stream().map(r -> {
                Reply reply = new Reply();
                reply.setHeadline(r.getHeadline());
                reply.setComment(r.getComment());

                User user = new User();
                user.setName(r.getUser().getName());
                user.setEmail(r.getUser().getEmail());
                user.setPhone(r.getUser().getPhone());
                user.setIsCustomer(r.getUser().getIsCustomer());

                reply.setUser(user);
                return reply;
            }).collect(Collectors.toList());
            ghl.setReply(replies);
        }

        return ghl;
    }
}
