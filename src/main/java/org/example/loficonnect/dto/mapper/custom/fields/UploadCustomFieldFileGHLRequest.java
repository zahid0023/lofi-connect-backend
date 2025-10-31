package org.example.loficonnect.dto.mapper.custom.fields;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.loficonnect.dto.request.custom.fields.UploadCustomFieldFileRequest;

@Data
public class UploadCustomFieldFileGHLRequest {
    private String id;
    private Integer maxFiles;

    public static UploadCustomFieldFileGHLRequest toGHLRequest(UploadCustomFieldFileRequest request) {
        UploadCustomFieldFileGHLRequest ghlRequest = new UploadCustomFieldFileGHLRequest();
        ghlRequest.setId(request.getId());
        ghlRequest.setMaxFiles(request.getMaxFiles());
        return ghlRequest;
    }
}