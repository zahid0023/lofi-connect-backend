package org.example.loficonnect.dto.mapper.customerprovider;

import lombok.Data;
import org.example.loficonnect.dto.request.customerprovider.CustomerProviderCreateRequest;

@Data
public class GoHighLevelCustomerProviderCreateRequest {
    private String name;
    private String description;
    private String paymentsUrl;
    private String queryUrl;
    private String imageUrl;

    public static GoHighLevelCustomerProviderCreateRequest fromRequest(CustomerProviderCreateRequest request) {
        GoHighLevelCustomerProviderCreateRequest ghl = new GoHighLevelCustomerProviderCreateRequest();
        ghl.setName(request.getName());
        ghl.setDescription(request.getDescription());
        ghl.setPaymentsUrl(request.getPaymentsUrl());
        ghl.setQueryUrl(request.getQueryUrl());
        ghl.setImageUrl(request.getImageUrl());
        return ghl;
    }
}
