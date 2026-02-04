package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.schedule.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "scheduleClient", url = "https://services.leadconnectorhq.com",configuration = FeignLoggingConfig.class)
public interface ScheduleClient {

    @PostMapping(
        value = "/invoices/schedule",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createSchedule(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelScheduleCreateRequest request
    );

    @GetMapping(
            value = "/invoices/schedule",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode listSchedules(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestParam("limit") String limit,
            @RequestParam("offset") String offset,
            @RequestParam(value = "endAt", required = false) String endAt,
            @RequestParam(value = "paymentMode", required = false) String paymentMode,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "startAt", required = false) String startAt,
            @RequestParam(value = "status", required = false) String status
    );

    @GetMapping(
            value = "/invoices/schedule/{scheduleId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSchedule(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @PutMapping(
            value = "/invoices/schedule/{scheduleId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateSchedule(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestBody GoHighLevelScheduleUpdateRequest request
    );

    @DeleteMapping(
            value = "/invoices/schedule/{scheduleId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteSchedule(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @PostMapping(
            value = "/invoices/schedule/{scheduleId}/updateAndSchedule",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateAndScheduleInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestBody GoHighLevelScheduleUpdateAndScheduleRequest request
    );

    @PostMapping(
            value = "/invoices/schedule/{scheduleId}/schedule",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode scheduleInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestBody GoHighLevelScheduleInvoiceRequest request
    );

    @PostMapping(
            value = "/invoices/schedule/{scheduleId}/auto-payment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode manageAutoPayment(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestBody GoHighLevelScheduleAutoPaymentRequest request
    );

    @PostMapping(
            value = "/invoices/schedule/{scheduleId}/cancel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode cancelScheduleInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("scheduleId") String scheduleId,
            @RequestBody GoHighLevelScheduleCancelRequest request
    );
}
