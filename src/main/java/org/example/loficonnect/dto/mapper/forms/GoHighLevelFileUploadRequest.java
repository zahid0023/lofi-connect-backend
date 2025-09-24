package org.example.loficonnect.dto.mapper.forms;

import lombok.Data;
import org.example.loficonnect.dto.request.forms.FileUploadRequest;

import java.util.Map;

@Data
public class GoHighLevelFileUploadRequest {
    private String contactId;
    private String locationId;
    private Map<String, Object> files;

    public static GoHighLevelFileUploadRequest fromRequest(FileUploadRequest request) {
        GoHighLevelFileUploadRequest ghlRequest = new GoHighLevelFileUploadRequest();

        ghlRequest.setContactId(request.getContactId());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setFiles(request.getFiles());

        return ghlRequest;
    }
}
