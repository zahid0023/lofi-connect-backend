package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.statictics.StaticticsRequest;
import org.example.loficonnect.service.StaticticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class StaticticsController {

    private final StaticticsService staticticsService;

    public StaticticsController(StaticticsService staticticsService) {
        this.staticticsService = staticticsService;
    }

    @AppKey
    @PostMapping("/statictics")
    public ResponseEntity<?> getStatistics(
        @RequestParam("location-id") String locationId,
        @RequestBody StaticticsRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(staticticsService.getStatistics(locationId, request));
    }
}
