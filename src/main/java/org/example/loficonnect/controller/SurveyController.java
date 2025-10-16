package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.SurveyService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @AppKey
    @GetMapping("/surveys/submissions")
    public ResponseEntity<?> getSurveySubmissions(
        @RequestParam("location-id") String locationId,
        @RequestParam(value = "survey-id", required = false) String surveyId,
        @RequestParam(value = "start-at", required = false) String startAt,
        @RequestParam(value = "end-at", required = false) String endAt,
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "limit", required = false) Integer limit,
        @RequestParam(value = "q", required = false) String q
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "surveyId", surveyId);
        MapUtil.putIfNotNull(queryParams, "startAt", startAt);
        MapUtil.putIfNotNull(queryParams, "endAt", endAt);
        MapUtil.putIfNotNull(queryParams, "page", page);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "q", q);

        return ResponseEntity.ok(surveyService.getSurveySubmissions(queryParams));
    }

    @AppKey
    @GetMapping("/surveys")
    public ResponseEntity<?> getSurveys(
            @RequestParam("location-id") String locationId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "skip", required = false) Integer skip,
            @RequestParam(value = "type", required = false) String type
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "type", type);

        return ResponseEntity.ok(surveyService.getSurveys(queryParams));
    }

}
