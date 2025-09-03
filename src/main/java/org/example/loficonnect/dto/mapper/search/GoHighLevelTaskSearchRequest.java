package org.example.loficonnect.dto.mapper.search;

import lombok.Data;
import org.example.loficonnect.dto.request.search.TaskSearchRequest;

import java.util.List;

@Data
public class GoHighLevelTaskSearchRequest {
    private List<String> contactId;
    private Boolean completed;
    private List<String> assignedTo;
    private String query;
    private Integer limit;
    private Integer skip;
    private String businessId;

    public static GoHighLevelTaskSearchRequest fromRequest(TaskSearchRequest request) {
        GoHighLevelTaskSearchRequest ghlRequest = new GoHighLevelTaskSearchRequest();
        ghlRequest.setContactId(request.getContactId());
        ghlRequest.setCompleted(request.getCompleted());
        ghlRequest.setAssignedTo(request.getAssignedTo());
        ghlRequest.setQuery(request.getQuery());
        ghlRequest.setLimit(request.getLimit());
        ghlRequest.setSkip(request.getSkip());
        ghlRequest.setBusinessId(request.getBusinessId());
        return ghlRequest;
    }
}
