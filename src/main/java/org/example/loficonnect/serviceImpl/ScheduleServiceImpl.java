package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.schedule.*;
import org.example.loficonnect.dto.request.schedule.*;
import org.example.loficonnect.feignclients.ScheduleClient;
import org.example.loficonnect.service.ScheduleService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleClient scheduleClient;
    private final AuthorizationServiceImpl authorizationService;

    public ScheduleServiceImpl(ScheduleClient scheduleClient, AuthorizationServiceImpl authorizationService) {
        this.scheduleClient = scheduleClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createSchedule(ScheduleCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelScheduleCreateRequest ghlRequest = GoHighLevelScheduleCreateRequest.fromRequest(request);
        return scheduleClient.createSchedule("Bearer " + accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode listSchedules(String altId, String altType, String limit, String offset,
                                  String endAt, String paymentMode, String search, String startAt, String status) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return scheduleClient.listSchedules("Bearer " + accessKey, version, altId, altType, limit, offset, endAt, paymentMode, search, startAt, status);
    }

    @Override
    public JsonNode getSchedule(String scheduleId, String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return scheduleClient.getSchedule("Bearer " + accessKey, version, scheduleId, altId, altType);
    }

    @Override
    public JsonNode updateSchedule(String scheduleId, ScheduleUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelScheduleUpdateRequest ghlRequest = GoHighLevelScheduleUpdateRequest.fromRequest(request);
        return scheduleClient.updateSchedule("Bearer " + accessKey, version, scheduleId, ghlRequest);
    }

    @Override
    public JsonNode deleteSchedule(String scheduleId, String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return scheduleClient.deleteSchedule("Bearer " + accessKey, version, scheduleId, altId, altType);
    }

    @Override
    public JsonNode updateAndScheduleInvoice(String scheduleId, ScheduleUpdateAndScheduleRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelScheduleUpdateAndScheduleRequest ghlRequest = GoHighLevelScheduleUpdateAndScheduleRequest.fromRequest(request);
        return scheduleClient.updateAndScheduleInvoice("Bearer " + accessKey, version, scheduleId, ghlRequest);
    }

    @Override
    public JsonNode scheduleInvoice(String scheduleId, ScheduleInvoiceRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelScheduleInvoiceRequest ghlRequest = GoHighLevelScheduleInvoiceRequest.fromRequest(request);
        return scheduleClient.scheduleInvoice("Bearer " + accessKey, version, scheduleId, ghlRequest);
    }

    @Override
    public JsonNode manageAutoPayment(String scheduleId, ScheduleAutoPaymentRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelScheduleAutoPaymentRequest ghlRequest = GoHighLevelScheduleAutoPaymentRequest.fromRequest(request);
        return scheduleClient.manageAutoPayment("Bearer " + accessKey, version, scheduleId, ghlRequest);
    }

    @Override
    public JsonNode cancelScheduledInvoice(String scheduleId, ScheduleCancelRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelScheduleCancelRequest ghlRequest = GoHighLevelScheduleCancelRequest.fromRequest(request);
        return scheduleClient.cancelScheduleInvoice("Bearer " + accessKey, version, scheduleId, ghlRequest);
    }
}
