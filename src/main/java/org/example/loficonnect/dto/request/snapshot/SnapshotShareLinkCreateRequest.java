package org.example.loficonnect.dto.request.snapshot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SnapshotShareLinkCreateRequest {
    private String snapshotId;
    private String shareType;
    private String relationshipNumber;
    private String shareLocationId;
}
