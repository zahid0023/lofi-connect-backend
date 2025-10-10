package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.InvoiceVoidRequest;

@Data
public class GoHighLevelInvoiceVoidRequest {
    private String altId;
    private String altType;

    public static GoHighLevelInvoiceVoidRequest fromRequest(InvoiceVoidRequest request) {
        GoHighLevelInvoiceVoidRequest ghlRequest = new GoHighLevelInvoiceVoidRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        return ghlRequest;
    }
}
