package org.example.loficonnect.dto.mapper.estimate;

import lombok.Data;
import org.example.loficonnect.dto.request.estimate.EstimateTemplateCreateRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoHighLevelEstimateTemplateCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private BusinessDetails businessDetails;
    private String currency;
    private List<Item> items;
    private Boolean liveMode;
    private Discount discount;
    private String termsNotes;
    private String title;
    private Boolean automaticTaxesEnabled;
    private Meta meta;
    private SendEstimateDetails sendEstimateDetails;
    private String estimateNumberPrefix;
    private List<Attachment> attachments;

    public static GoHighLevelEstimateTemplateCreateRequest fromRequest(EstimateTemplateCreateRequest request) {
        GoHighLevelEstimateTemplateCreateRequest ghlRequest = new GoHighLevelEstimateTemplateCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        ghlRequest.setCurrency(request.getCurrency());
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());
        ghlRequest.setEstimateNumberPrefix(request.getEstimateNumberPrefix());

        if (request.getBusinessDetails() != null) {
            BusinessDetails businessDetails = new BusinessDetails();
            businessDetails.setLogoUrl(request.getBusinessDetails().getLogoUrl());
            businessDetails.setName(request.getBusinessDetails().getName());
            businessDetails.setPhoneNo(request.getBusinessDetails().getPhoneNo());
            businessDetails.setAddress(request.getBusinessDetails().getAddress());
            businessDetails.setWebsite(request.getBusinessDetails().getWebsite());
            businessDetails.setCustomValues(request.getBusinessDetails().getCustomValues());
            ghlRequest.setBusinessDetails(businessDetails);
        }

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<Item> items = new ArrayList<>();
            for (EstimateTemplateCreateRequest.Item item : request.getItems()) {
                Item mappedItem = new Item();
                mappedItem.setName(item.getName());
                mappedItem.setDescription(item.getDescription());
                mappedItem.setProductId(item.getProductId());
                mappedItem.setPriceId(item.getPriceId());
                mappedItem.setCurrency(item.getCurrency());
                mappedItem.setAmount(item.getAmount());
                mappedItem.setQty(item.getQty());
                items.add(mappedItem);
            }
            ghlRequest.setItems(items);
        }

        if (request.getDiscount() != null) {
            Discount discount = new Discount();
            discount.setValue(request.getDiscount().getValue());
            discount.setType(request.getDiscount().getType());
            discount.setValidOnProductIds(request.getDiscount().getValidOnProductIds());
            ghlRequest.setDiscount(discount);
        }

        if (request.getSendEstimateDetails() != null) {
            SendEstimateDetails sendEstimateDetails = new SendEstimateDetails();
            sendEstimateDetails.setAltId(request.getSendEstimateDetails().getAltId());
            sendEstimateDetails.setAltType(request.getSendEstimateDetails().getAltType());
            sendEstimateDetails.setAction(request.getSendEstimateDetails().getAction());
            sendEstimateDetails.setLiveMode(request.getSendEstimateDetails().getLiveMode());
            sendEstimateDetails.setUserId(request.getSendEstimateDetails().getUserId());

            SentFrom sentFrom = new SentFrom();
            sentFrom.setFromName(request.getSendEstimateDetails().getSentFrom().getFromName());
            sentFrom.setFromEmail(request.getSendEstimateDetails().getSentFrom().getFromEmail());
            sendEstimateDetails.setSentFrom(sentFrom);

            sendEstimateDetails.setEstimateName(request.getSendEstimateDetails().getEstimateName());
            ghlRequest.setSendEstimateDetails(sendEstimateDetails);
        }

        if (request.getMeta() != null) {
            Meta meta = new Meta();
            meta.setKey(request.getMeta().getKey());
            meta.setValue(request.getMeta().getValue());
            ghlRequest.setMeta(meta);
        }

        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<Attachment> attachments = new ArrayList<>();
            for (EstimateTemplateCreateRequest.Attachment attachment : request.getAttachments()) {
                Attachment mappedAttachment = new Attachment();
                mappedAttachment.setId(attachment.getId());
                mappedAttachment.setName(attachment.getName());
                mappedAttachment.setUrl(attachment.getUrl());
                mappedAttachment.setType(attachment.getType());
                mappedAttachment.setSize(attachment.getSize());
                attachments.add(mappedAttachment);
            }
            ghlRequest.setAttachments(attachments);
        }

        return ghlRequest;
    }

    @Data
    public static class BusinessDetails {
        private String logoUrl;
        private String name;
        private String phoneNo;
        private String address;
        private String website;
        private List<String> customValues;
    }

    @Data
    public static class Item {
        private String name;
        private String description;
        private String productId;
        private String priceId;
        private String currency;
        private Double amount;
        private Integer qty;
    }

    @Data
    public static class Discount {
        private Double value;
        private String type;
        private List<String> validOnProductIds;
    }

    @Data
    public static class SendEstimateDetails {
        private String altId;
        private String altType;
        private String action;
        private Boolean liveMode;
        private String userId;
        private SentFrom sentFrom;
        private String estimateName;
    }

    @Data
    public static class SentFrom {
        private String fromName;
        private String fromEmail;
    }

    @Data
    public static class Meta {
        private String key;
        private String value;
    }

    @Data
    public static class Attachment {
        private String id;
        private String name;
        private String url;
        private String type;
        private Integer size;
    }
}