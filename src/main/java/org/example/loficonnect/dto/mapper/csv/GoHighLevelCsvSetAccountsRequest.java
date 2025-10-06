package org.example.loficonnect.dto.mapper.csv;

import lombok.Data;
import org.example.loficonnect.dto.request.csv.CsvSetAccountsRequest;

import java.util.List;

@Data
public class GoHighLevelCsvSetAccountsRequest {
    private List<String> accountIds;
    private String filePath;
    private Integer rowsCount;
    private String fileName;
    private String approver;
    private String userId;

    public static GoHighLevelCsvSetAccountsRequest fromRequest(CsvSetAccountsRequest request) {
        GoHighLevelCsvSetAccountsRequest ghlRequest = new GoHighLevelCsvSetAccountsRequest();
        ghlRequest.setAccountIds(request.getAccountIds());
        ghlRequest.setFilePath(request.getFilePath());
        ghlRequest.setRowsCount(request.getRowsCount());
        ghlRequest.setFileName(request.getFileName());
        ghlRequest.setApprover(request.getApprover());
        ghlRequest.setUserId(request.getUserId());
        return ghlRequest;
    }
}
