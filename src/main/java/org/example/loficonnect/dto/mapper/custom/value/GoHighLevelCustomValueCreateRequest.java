package org.example.loficonnect.dto.mapper.custom.value;

import lombok.Data;
import org.example.loficonnect.dto.request.custom.value.CustomValueCreateRequest;

@Data
public class GoHighLevelCustomValueCreateRequest {
    private String name;
    private String value;

    public static GoHighLevelCustomValueCreateRequest fromRequest(CustomValueCreateRequest request) {
        GoHighLevelCustomValueCreateRequest ghlRequest = new GoHighLevelCustomValueCreateRequest();
        ghlRequest.setName(request.getName());
        ghlRequest.setValue(request.getValue());
        return ghlRequest;
    }
}
