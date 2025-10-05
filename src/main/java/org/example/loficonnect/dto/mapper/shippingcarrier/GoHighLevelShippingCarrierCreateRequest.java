package org.example.loficonnect.dto.mapper.shippingcarrier;

import lombok.Data;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelShippingCarrierCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private String callbackUrl;
    private List<Service> services;
    private Boolean allowsMultipleServiceSelection;

    @Data
    public static class Service {
        private String name;
        private String value;
    }

    public static GoHighLevelShippingCarrierCreateRequest fromRequest(ShippingCarrierCreateRequest request) {
        GoHighLevelShippingCarrierCreateRequest ghlRequest = new GoHighLevelShippingCarrierCreateRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setCallbackUrl(request.getCallbackUrl());
        ghlRequest.setAllowsMultipleServiceSelection(request.getAllowsMultipleServiceSelection());

        if (request.getServices() != null) {
            List<Service> mappedServices = request.getServices().stream().map(serviceReq -> {
                Service service = new Service();
                service.setName(serviceReq.getName());
                service.setValue(serviceReq.getValue());
                return service;
            }).collect(Collectors.toList());
            ghlRequest.setServices(mappedServices);
        }

        return ghlRequest;
    }
}
