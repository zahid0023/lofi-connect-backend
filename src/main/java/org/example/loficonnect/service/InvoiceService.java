package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.invoice.*;

import java.util.Map;

public interface InvoiceService {
    JsonNode generateInvoiceNumber(String altId, String altType);
    JsonNode getInvoice(String invoiceId, String altId, String altType);
    JsonNode updateInvoice(String invoiceId, InvoiceUpdateRequest request);
    JsonNode deleteInvoice(String invoiceId, String altId, String altType);
    JsonNode updateLateFeesConfiguration(String invoiceId, String altId, String altType, InvoiceLateFeesConfigurationRequest request);
    JsonNode voidInvoice(String invoiceId, String altId, String altType, InvoiceVoidRequest request);
    JsonNode sendInvoice(String invoiceId, String altId, String altType, InvoiceSendRequest request);
    JsonNode recordPayment(String invoiceId, String altId, String altType, RecordPaymentRequest request);
    JsonNode updateInvoiceLastVisitedAt(InvoiceUpdateLastVisitedAtRequest request);
    JsonNode createInvoice(InvoiceCreateRequest request);
    JsonNode listInvoices(Map<String, String> queryParams);
}
