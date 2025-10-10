package org.example.loficonnect.dto.mapper.post;

import lombok.Data;
import org.example.loficonnect.dto.request.post.BulkPostDeleteRequest;

import java.util.List;

@Data
public class GoHighLevelBulkPostDeleteRequest {
    private List<String> postIds;

    public static GoHighLevelBulkPostDeleteRequest fromRequest(BulkPostDeleteRequest request) {
        GoHighLevelBulkPostDeleteRequest ghl = new GoHighLevelBulkPostDeleteRequest();
        ghl.setPostIds(request.getPost_ids());
        return ghl;
    }
}
