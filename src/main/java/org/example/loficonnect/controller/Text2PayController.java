package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.text2pay.Text2PayCreateRequest;
import org.example.loficonnect.service.Text2PayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ghl")
public class Text2PayController {

    private final Text2PayService text2PayService;

    public Text2PayController(Text2PayService text2PayService) {
        this.text2PayService = text2PayService;
    }

    @AppKey
    @PostMapping("/text2pay")
    public ResponseEntity<?> createText2PayInvoice(@RequestBody Text2PayCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(text2PayService.createText2PayInvoice(request));
    }
}
