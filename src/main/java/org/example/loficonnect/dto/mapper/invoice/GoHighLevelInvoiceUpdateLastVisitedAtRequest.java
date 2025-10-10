package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.InvoiceUpdateLastVisitedAtRequest;

@Data
public class GoHighLevelInvoiceUpdateLastVisitedAtRequest {
    private String invoiceId;

    public static GoHighLevelInvoiceUpdateLastVisitedAtRequest fromRequest(InvoiceUpdateLastVisitedAtRequest request) {
        GoHighLevelInvoiceUpdateLastVisitedAtRequest ghlRequest = new GoHighLevelInvoiceUpdateLastVisitedAtRequest();
        ghlRequest.setInvoiceId(request.getInvoiceId());
        return ghlRequest;
    }
}
