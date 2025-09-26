package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.schedule.*;

public interface ScheduleService {
    JsonNode createSchedule(ScheduleCreateRequest request);
    JsonNode listSchedules(String altId, String altType, String limit, String offset,
                           String endAt, String paymentMode, String search, String startAt, String status);
    JsonNode getSchedule(String scheduleId, String altId, String altType);
    JsonNode updateSchedule(String scheduleId, ScheduleUpdateRequest request);
    JsonNode deleteSchedule(String scheduleId, String altId, String altType);
    JsonNode updateAndScheduleInvoice(String scheduleId, ScheduleUpdateAndScheduleRequest request);
    JsonNode scheduleInvoice(String scheduleId, ScheduleInvoiceRequest request);
    JsonNode manageAutoPayment(String scheduleId, ScheduleAutoPaymentRequest request);
    JsonNode cancelScheduledInvoice(String scheduleId, ScheduleCancelRequest request);
}
