package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.TimeZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ghl")
public class TimeZoneController {
    private final TimeZoneService timeZoneService;

    public TimeZoneController(TimeZoneService timeZoneService) {
        this.timeZoneService = timeZoneService;
    }

    @AppKey
    @GetMapping("/locations/{location-id}/timezones")
    public ResponseEntity<?> getTimezones(
            @PathVariable("location-id") String locationId
    ) {
        return ResponseEntity.ok(timeZoneService.getTimezones(locationId));
    }

}
