package org.example.loficonnect.dto.mapper.post;

import lombok.Data;
import org.example.loficonnect.dto.request.post.PostListRequest;

@Data
public class GoHighLevelPostListRequest {
    private String type;
    private String accounts;
    private String skip;
    private String limit;
    private String fromDate;
    private String toDate;
    private String includeUsers;
    private String postType;

    public static GoHighLevelPostListRequest fromRequest(PostListRequest request) {
        GoHighLevelPostListRequest ghlRequest = new GoHighLevelPostListRequest();
        ghlRequest.setType(request.getType());
        ghlRequest.setAccounts(request.getAccounts());
        ghlRequest.setSkip(request.getSkip());
        ghlRequest.setLimit(request.getLimit());
        ghlRequest.setFromDate(request.getFrom_date());
        ghlRequest.setToDate(request.getTo_date());
        ghlRequest.setIncludeUsers(request.getInclude_users());
        ghlRequest.setPostType(request.getPost_type());
        return ghlRequest;
    }
}
