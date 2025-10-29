package org.example.loficonnect.dto.mapper.usersearch;

import lombok.Data;
import org.example.loficonnect.dto.request.usersearch.UserSearchRequest;

import java.util.List;

@Data
public class GoHighLevelUserSearchRequest {
    private String companyId;
    private List<String> emails;
    private Boolean deleted;
    private String skip;
    private String limit;
    private String projection;

    public static GoHighLevelUserSearchRequest fromRequest(UserSearchRequest request) {
        GoHighLevelUserSearchRequest ghlRequest = new GoHighLevelUserSearchRequest();

        ghlRequest.setCompanyId(request.getCompanyId());
        ghlRequest.setEmails(request.getEmails());
        ghlRequest.setDeleted(request.getDeleted());
        ghlRequest.setSkip(request.getSkip());
        ghlRequest.setLimit(request.getLimit());
        ghlRequest.setProjection(request.getProjection());

        return ghlRequest;
    }
}
