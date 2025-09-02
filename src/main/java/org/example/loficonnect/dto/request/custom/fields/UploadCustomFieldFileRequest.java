package org.example.loficonnect.dto.request.custom.fields;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadCustomFieldFileRequest {
    private String id;        // Custom Field ID
    private Integer maxFiles; // Max files allowed
    private MultipartFile file; // File to upload
}
