package org.example.loficonnect.dto.request.csv;

import lombok.Data;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CsvSetAccountsRequest {
    private List<String> accountIds;
    private String filePath;
    private Integer rowsCount;
    private String fileName;
    private String approver;
    private String userId;
}
