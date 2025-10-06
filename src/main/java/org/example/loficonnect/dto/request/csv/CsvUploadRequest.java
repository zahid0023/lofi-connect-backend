package org.example.loficonnect.dto.request.csv;

import lombok.Data;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CsvUploadRequest {
    private MultipartFile file;
}
