package org.example.loficonnect.dto.mapper.post;

import lombok.Data;
import org.example.loficonnect.dto.request.post.PostUpdateRequest;

import java.util.List;
import java.util.Map;

@Data
public class GoHighLevelPostUpdateRequest {
    private List<String> accountIds;
    private String summary;
    private List<Map<String, Object>> media;
    private String status;
    private String scheduleDate;
    private String createdBy;
    private String followUpComment;
    private Map<String, Object> ogTagsDetails;
    private String type;
    private Map<String, Object> postApprovalDetails;
    private Boolean scheduleTimeUpdated;
    private List<String> tags;
    private String categoryId;
    private Map<String, Object> tiktokPostDetails;
    private Map<String, Object> gmbPostDetails;
    private String userId;

    public static GoHighLevelPostUpdateRequest fromRequest(PostUpdateRequest request) {
        GoHighLevelPostUpdateRequest ghl = new GoHighLevelPostUpdateRequest();
        ghl.setAccountIds(request.getAccount_ids());
        ghl.setSummary(request.getSummary());
        ghl.setMedia(request.getMedia());
        ghl.setStatus(request.getStatus());
        ghl.setScheduleDate(request.getSchedule_date());
        ghl.setCreatedBy(request.getCreated_by());
        ghl.setFollowUpComment(request.getFollow_up_comment());
        ghl.setOgTagsDetails(request.getOg_tags_details());
        ghl.setType(request.getType());
        ghl.setPostApprovalDetails(request.getPost_approval_details());
        ghl.setScheduleTimeUpdated(request.getSchedule_time_updated());
        ghl.setTags(request.getTags());
        ghl.setCategoryId(request.getCategory_id());
        ghl.setTiktokPostDetails(request.getTiktok_post_details());
        ghl.setGmbPostDetails(request.getGmb_post_details());
        ghl.setUserId(request.getUser_id());
        return ghl;
    }
}
