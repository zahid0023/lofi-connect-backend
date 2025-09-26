package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.schedule.*;
import org.example.loficonnect.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(request));
    }

    @AppKey
    @GetMapping("/list")
    public ResponseEntity<JsonNode> listSchedules(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset,
            @RequestParam(value = "end-at", required = false) String endAt,
            @RequestParam(value = "payment-mode", required = false) String paymentMode,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start-at", required = false) String startAt,
            @RequestParam(value = "status", required = false) String status
    ) {
        JsonNode schedules = scheduleService.listSchedules(altId, altType, limit, offset, endAt, paymentMode, search, startAt, status);
        return ResponseEntity.ok(schedules);
    }

    @AppKey
    @GetMapping("/{schedule-id}")
    public ResponseEntity<JsonNode> getSchedule(
            @PathVariable("schedule-id") String scheduleId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode schedule = scheduleService.getSchedule(scheduleId, altId, altType);
        return ResponseEntity.ok(schedule);
    }

    @AppKey
    @PutMapping("/{schedule-id}")
    public ResponseEntity<JsonNode> updateSchedule(
            @PathVariable("schedule-id") String scheduleId,
            @RequestBody ScheduleUpdateRequest request
    ) {
        JsonNode updatedSchedule = scheduleService.updateSchedule(scheduleId, request);
        return ResponseEntity.ok(updatedSchedule);
    }

    @AppKey
    @DeleteMapping("/{schedule-id}")
    public ResponseEntity<JsonNode> deleteSchedule(
            @PathVariable("schedule-id") String scheduleId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode deletedSchedule = scheduleService.deleteSchedule(scheduleId, altId, altType);
        return ResponseEntity.ok(deletedSchedule);
    }

    @AppKey
    @PostMapping("/{schedule-id}/update-and-schedule")
    public ResponseEntity<JsonNode> updateAndScheduleInvoice(
            @PathVariable("schedule-id") String scheduleId,
            @RequestBody ScheduleUpdateAndScheduleRequest request
    ) {
        JsonNode updatedSchedule = scheduleService.updateAndScheduleInvoice(scheduleId, request);
        return ResponseEntity.ok(updatedSchedule);
    }

    @AppKey
    @PostMapping("/{schedule-id}/schedule")
    public ResponseEntity<JsonNode> scheduleInvoice(
            @PathVariable("schedule-id") String scheduleId,
            @RequestBody ScheduleInvoiceRequest request
    ) {
        JsonNode scheduledInvoice = scheduleService.scheduleInvoice(scheduleId, request);
        return ResponseEntity.ok(scheduledInvoice);
    }

    @AppKey
    @PostMapping("/{schedule-id}/auto-payment")
    public ResponseEntity<JsonNode> manageAutoPayment(
            @PathVariable("schedule-id") String scheduleId,
            @RequestBody ScheduleAutoPaymentRequest request
    ) {
        JsonNode response = scheduleService.manageAutoPayment(scheduleId, request);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PostMapping("/{schedule-id}/cancel")
    public ResponseEntity<JsonNode> cancelScheduledInvoice(
            @PathVariable("schedule-id") String scheduleId,
            @RequestBody ScheduleCancelRequest request
    ) {
        JsonNode response = scheduleService.cancelScheduledInvoice(scheduleId, request);
        return ResponseEntity.ok(response);
    }
}
