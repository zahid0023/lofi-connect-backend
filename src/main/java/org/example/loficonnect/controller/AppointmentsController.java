package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.AppointmentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class AppointmentsController {
    private final AppointmentsService appointmentsService;

    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @AppKey
    @GetMapping("/contacts/{contact-id}/appointments")
    public ResponseEntity<?> getContactAppointments(
            @PathVariable("contact-id") String contactId
    ) {
        Map<String, Object> queryParams = new HashMap<>();

        return ResponseEntity.ok(appointmentsService.getContactAppointments(contactId, queryParams));
    }

}
