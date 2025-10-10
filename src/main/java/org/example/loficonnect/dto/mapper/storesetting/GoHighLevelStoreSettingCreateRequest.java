package org.example.loficonnect.dto.mapper.storesetting;

import lombok.Data;
import org.example.loficonnect.dto.request.storesetting.StoreSettingCreateRequest;

@Data
public class GoHighLevelStoreSettingCreateRequest {
    private String altId;
    private String altType;
    private ShippingOrigin shippingOrigin;
    private StoreOrderNotification storeOrderNotification;
    private StoreOrderFulfillmentNotification storeOrderFulfillmentNotification;

    @Data
    public static class ShippingOrigin {
        private String name;
        private String country;
        private String state;
        private String city;
        private String street1;
        private String street2;
        private String zip;
        private String phone;
        private String email;
    }

    @Data
    public static class StoreOrderNotification {
        private Boolean enabled;
        private String subject;
        private String emailTemplateId;
        private String defaultEmailTemplateId;
    }

    @Data
    public static class StoreOrderFulfillmentNotification {
        private Boolean enabled;
        private String subject;
        private String emailTemplateId;
        private String defaultEmailTemplateId;
    }

    public static GoHighLevelStoreSettingCreateRequest fromRequest(StoreSettingCreateRequest request) {
        GoHighLevelStoreSettingCreateRequest ghlRequest = new GoHighLevelStoreSettingCreateRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());

        if (request.getShippingOrigin() != null) {
            ShippingOrigin origin = new ShippingOrigin();
            origin.setName(request.getShippingOrigin().getName());
            origin.setCountry(request.getShippingOrigin().getCountry());
            origin.setState(request.getShippingOrigin().getState());
            origin.setCity(request.getShippingOrigin().getCity());
            origin.setStreet1(request.getShippingOrigin().getStreet1());
            origin.setStreet2(request.getShippingOrigin().getStreet2());
            origin.setZip(request.getShippingOrigin().getZip());
            origin.setPhone(request.getShippingOrigin().getPhone());
            origin.setEmail(request.getShippingOrigin().getEmail());
            ghlRequest.setShippingOrigin(origin);
        }

        if (request.getStoreOrderNotification() != null) {
            StoreOrderNotification noti = new StoreOrderNotification();
            noti.setEnabled(request.getStoreOrderNotification().getEnabled());
            noti.setSubject(request.getStoreOrderNotification().getSubject());
            noti.setEmailTemplateId(request.getStoreOrderNotification().getEmailTemplateId());
            noti.setDefaultEmailTemplateId(request.getStoreOrderNotification().getDefaultEmailTemplateId());
            ghlRequest.setStoreOrderNotification(noti);
        }

        if (request.getStoreOrderFulfillmentNotification() != null) {
            StoreOrderFulfillmentNotification notiF = new StoreOrderFulfillmentNotification();
            notiF.setEnabled(request.getStoreOrderFulfillmentNotification().getEnabled());
            notiF.setSubject(request.getStoreOrderFulfillmentNotification().getSubject());
            notiF.setEmailTemplateId(request.getStoreOrderFulfillmentNotification().getEmailTemplateId());
            notiF.setDefaultEmailTemplateId(request.getStoreOrderFulfillmentNotification().getDefaultEmailTemplateId());
            ghlRequest.setStoreOrderFulfillmentNotification(notiF);
        }

        return ghlRequest;
    }
}
