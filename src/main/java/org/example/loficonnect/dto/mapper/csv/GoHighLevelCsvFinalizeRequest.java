package org.example.loficonnect.dto.mapper.csv;

import lombok.Data;
import org.example.loficonnect.dto.request.csv.CsvFinalizeRequest;

@Data
public class GoHighLevelCsvFinalizeRequest {
    private String userId;

    public static GoHighLevelCsvFinalizeRequest fromRequest(CsvFinalizeRequest request) {
        GoHighLevelCsvFinalizeRequest ghlRequest = new GoHighLevelCsvFinalizeRequest();
        ghlRequest.setUserId(request.getUserId());
        return ghlRequest;
    }
}
