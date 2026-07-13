package org.example.loficonnect.usage.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConnectionInfo {
    private String platform;        // e.g. "GHL"
    private String companyId;       // GHL agency company ID
    private String subaccountName;  // human-readable name of the sub-account / agency
    private String locationId;      // GHL location ID (sub-account scope)
    private String userType;        // "Agency" or "Location"
}
