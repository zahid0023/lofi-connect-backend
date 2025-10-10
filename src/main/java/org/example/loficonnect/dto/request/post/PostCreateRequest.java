package org.example.loficonnect.dto.request.post;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostCreateRequest {
    private List<String> account_ids;
    private String summary;
    private List<Map<String, Object>> media;
    private String status;
    private String schedule_date;
    private String created_by;
    private String follow_up_comment;
    private Map<String, Object> og_tags_details;
    private String type;
    private Map<String, Object> post_approval_details;
    private Boolean schedule_time_updated;
    private List<String> tags;
    private String category_id;
    private Map<String, Object> tiktok_post_details;
    private Map<String, Object> gmb_post_details;
    private String user_id;
}
