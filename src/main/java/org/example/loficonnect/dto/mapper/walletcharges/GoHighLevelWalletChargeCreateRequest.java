package org.example.loficonnect.dto.mapper.walletcharges;

import lombok.Data;
import org.example.loficonnect.dto.request.walletcharges.WalletChargeCreateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class GoHighLevelWalletChargeCreateRequest {
    private String appId;
    private String meterId;
    private String eventId;
    private String userId;
    private String locationId;
    private String companyId;
    private String description;
    private String units;
    private ZonedDateTime eventTime;

    public static GoHighLevelWalletChargeCreateRequest fromRequest(WalletChargeCreateRequest request) {
        GoHighLevelWalletChargeCreateRequest ghl = new GoHighLevelWalletChargeCreateRequest();
        ghl.setAppId(request.getAppId());
        ghl.setMeterId(request.getMeterId());
        ghl.setEventId(request.getEventId());
        ghl.setUserId(request.getUserId());
        ghl.setLocationId(request.getLocationId());
        ghl.setCompanyId(request.getCompanyId());
        ghl.setDescription(request.getDescription());
        ghl.setUnits(request.getUnits());

        LocalDate date = request.getEventDate();
        LocalTime time = request.getEventTime();
        String zoneId = request.getTimeZone();

        if (date != null && time != null && zoneId != null) {
            ZoneId zone;
            try {
                zone = zoneId.isBlank() ? ZoneId.systemDefault() : ZoneId.of(zoneId);
            } catch (Exception e) {
                zone = ZoneId.systemDefault();
            }
            ghl.setEventTime(ZonedDateTime.of(LocalDateTime.of(date, time), zone));
        } else {
            ghl.setEventTime(null);
        }

        return ghl;
    }
}
