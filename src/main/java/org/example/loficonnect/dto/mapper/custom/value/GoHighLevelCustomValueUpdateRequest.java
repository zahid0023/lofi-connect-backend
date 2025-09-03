package org.example.loficonnect.dto.mapper.custom.value;

import lombok.Data;
import org.example.loficonnect.dto.request.custom.value.CustomValueUpdateRequest;

@Data
public class GoHighLevelCustomValueUpdateRequest {
    private String name;
    private String value;

    public static GoHighLevelCustomValueUpdateRequest fromRequest(CustomValueUpdateRequest request) {
        GoHighLevelCustomValueUpdateRequest ghlRequest = new GoHighLevelCustomValueUpdateRequest();
        ghlRequest.setName(request.getName());
        ghlRequest.setValue(request.getValue());
        return ghlRequest;
    }
}
