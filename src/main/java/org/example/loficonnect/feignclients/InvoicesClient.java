package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.invoice.*;
import org.example.loficonnect.dto.request.invoice.InvoiceLateFeesConfigurationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "invoicesClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface InvoicesClient {

    @GetMapping(
            value = "/invoices/generate-invoice-number",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode generateInvoiceNumber(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @GetMapping(
            value = "/invoices/{invoiceId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @PutMapping(
            value = "/invoices/{invoiceId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestBody GoHighLevelInvoiceUpdateRequest request
    );

    @DeleteMapping(
            value = "/invoices/{invoiceId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );

    @PatchMapping(
            value = "/invoices/{invoiceId}/late-fees-configuration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateLateFeesConfiguration(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestBody InvoiceLateFeesConfigurationRequest request
    );

    @PostMapping(
            value = "/invoices/{invoiceId}/void",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode voidInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestBody GoHighLevelInvoiceVoidRequest request
    );

    @PostMapping(
            value = "/invoices/{invoiceId}/send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode sendInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestBody GoHighLevelInvoiceSendRequest request
    );

    @PostMapping(
            value = "/invoices/{invoiceId}/record-payment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode recordPayment(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("invoiceId") String invoiceId,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType,
            @RequestBody GoHighLevelRecordPaymentRequest request
    );

    @PatchMapping(
            value = "/invoices/stats/last-visited-at",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateInvoiceLastVisitedAt(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelInvoiceUpdateLastVisitedAtRequest request
    );

    @PostMapping(
            value = "/invoices/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createInvoice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelInvoiceCreateRequest request
    );

    @GetMapping(
            value = "/invoices",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode listInvoices(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, String> queryParams
    );
}
