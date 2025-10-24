package org.example.loficonnect.dto.request.walletcharges;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WalletChargeCreateRequest {
    private String appId;
    private String meterId;
    private String eventId;
    private String userId;
    private String locationId;
    private String companyId;
    private String description;
    private Double units;

    private LocalDate eventDate;
    private LocalTime eventTime;
    private String timeZone;
}
