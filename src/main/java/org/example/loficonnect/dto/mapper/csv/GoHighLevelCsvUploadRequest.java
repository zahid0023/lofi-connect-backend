package org.example.loficonnect.dto.mapper.csv;

import lombok.Data;
import org.example.loficonnect.dto.request.csv.CsvUploadRequest;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GoHighLevelCsvUploadRequest {
    private MultipartFile file;

    public static GoHighLevelCsvUploadRequest fromRequest(CsvUploadRequest request) {
        GoHighLevelCsvUploadRequest ghlRequest = new GoHighLevelCsvUploadRequest();
        ghlRequest.setFile(request.getFile());
        return ghlRequest;
    }
}
