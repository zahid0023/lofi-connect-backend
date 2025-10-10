package org.example.loficonnect.dto.mapper.schedule;

import lombok.Data;
import org.example.loficonnect.dto.request.schedule.ScheduleCancelRequest;

@Data
public class GoHighLevelScheduleCancelRequest {

    private String altId;
    private String altType;

    public static GoHighLevelScheduleCancelRequest fromRequest(ScheduleCancelRequest request) {
        GoHighLevelScheduleCancelRequest ghlRequest = new GoHighLevelScheduleCancelRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        return ghlRequest;
    }
}
