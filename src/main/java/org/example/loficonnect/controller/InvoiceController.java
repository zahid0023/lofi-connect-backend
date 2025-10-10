package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.invoice.*;
import org.example.loficonnect.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @AppKey
    @GetMapping("/generate-invoice-number")
    public ResponseEntity<?> generateInvoiceNumber(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode invoiceNumber = invoiceService.generateInvoiceNumber(altId, altType);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceNumber);
    }

    @AppKey
    @GetMapping("/invoices/{invoice-id}")
    public ResponseEntity<?> getInvoice(
            @PathVariable("invoice-id") String invoiceId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode invoice = invoiceService.getInvoice(invoiceId, altId, altType);
        return ResponseEntity.status(HttpStatus.OK).body(invoice);
    }

    @AppKey
    @PutMapping("/invoices/{invoice-id}")
    public ResponseEntity<?> updateInvoice(
            @PathVariable("invoice-id") String invoiceId,
            @RequestBody InvoiceUpdateRequest request
    ) {
        JsonNode updatedInvoice = invoiceService.updateInvoice(invoiceId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedInvoice);
    }

    @AppKey
    @DeleteMapping("/invoices/{invoice-id}")
    public ResponseEntity<?> deleteInvoice(
            @PathVariable("invoice-id") String invoiceId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        JsonNode result = invoiceService.deleteInvoice(invoiceId, altId, altType);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @AppKey
    @PatchMapping("/invoices/{invoice-id}/late-fees-configuration")
    public ResponseEntity<?> updateLateFeesConfiguration(
            @PathVariable("invoice-id") String invoiceId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestBody InvoiceLateFeesConfigurationRequest request
    ) {
        JsonNode result = invoiceService.updateLateFeesConfiguration(invoiceId, altId, altType, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @AppKey
    @PostMapping("/invoices/{invoice-id}/void")
    public ResponseEntity<?> voidInvoice(
            @PathVariable("invoice-id") String invoiceId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestBody InvoiceVoidRequest request
    ) {
        JsonNode result = invoiceService.voidInvoice(invoiceId, altId, altType, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @AppKey
    @PostMapping("/invoices/{invoice-id}/send")
    public ResponseEntity<?> sendInvoice(
            @PathVariable("invoice-id") String invoiceId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestBody InvoiceSendRequest request
    ) {
        JsonNode result = invoiceService.sendInvoice(invoiceId, altId, altType, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @AppKey
    @PostMapping("/invoices/{invoice-id}/record-payment")
    public ResponseEntity<?> recordPayment(
            @PathVariable("invoice-id") String invoiceId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestBody RecordPaymentRequest request
    ) {
        JsonNode result = invoiceService.recordPayment(invoiceId, altId, altType, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @AppKey
    @PatchMapping("/invoices/stats/last-visited-at")
    public ResponseEntity<?> updateInvoiceLastVisitedAt(
            @RequestBody InvoiceUpdateLastVisitedAtRequest request
    ) {
        JsonNode result = invoiceService.updateInvoiceLastVisitedAt(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @AppKey
    @PostMapping("/invoices")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceCreateRequest request) {
        JsonNode result = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<?> listInvoices(@RequestParam Map<String, String> queryParams) {
        return ResponseEntity.ok(invoiceService.listInvoices(queryParams));
    }
}
