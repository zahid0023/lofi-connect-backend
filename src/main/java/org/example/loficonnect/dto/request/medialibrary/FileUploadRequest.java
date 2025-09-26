package org.example.loficonnect.dto.request.medialibrary;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileUploadRequest {
    private MultipartFile file;
    private Boolean hosted;
    private String fileUrl;
    private String name;
    private String parentId;
}
