package org.example.loficonnect.dto.mapper.task;

import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskCompletedRequest;

@Data
public class GoHighLevelTaskCompletedRequest {
    private Boolean completed;

    public static GoHighLevelTaskCompletedRequest fromRequest(TaskCompletedRequest request) {
        GoHighLevelTaskCompletedRequest ghl = new GoHighLevelTaskCompletedRequest();
        ghl.setCompleted(request.getCompleted());
        return ghl;
    }
}
