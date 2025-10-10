package org.example.loficonnect.dto.mapper.medialibrary;

import lombok.Data;
import org.example.loficonnect.dto.request.medialibrary.FileUploadRequest;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GoHighLevelFileUploadRequest {
    private MultipartFile file;
    private Boolean hosted;
    private String fileUrl;
    private String name;
    private String parentId;

    public static GoHighLevelFileUploadRequest fromRequest(FileUploadRequest request) {
        GoHighLevelFileUploadRequest ghlRequest = new GoHighLevelFileUploadRequest();
        ghlRequest.setFile(request.getFile());
        ghlRequest.setHosted(request.getHosted());
        ghlRequest.setFileUrl(request.getFileUrl());
        ghlRequest.setName(request.getName());
        ghlRequest.setParentId(request.getParentId());
        return ghlRequest;
    }
}
