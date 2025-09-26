package org.example.loficonnect.dto.mapper.estimate;

import lombok.Data;
import org.example.loficonnect.dto.request.estimate.InvoiceCreateRequest;

@Data
public class GoHighLevelInvoiceCreateRequest {
    private String altId;
    private String altType;
    private Boolean markAsInvoiced;
    private String version;

    public static GoHighLevelInvoiceCreateRequest fromRequest(InvoiceCreateRequest request) {
        GoHighLevelInvoiceCreateRequest ghlRequest = new GoHighLevelInvoiceCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setMarkAsInvoiced(request.getMarkAsInvoiced());
        ghlRequest.setVersion(request.getVersion());

        return ghlRequest;
    }
}
