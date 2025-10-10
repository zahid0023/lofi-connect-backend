package org.example.loficonnect.dto.mapper.estimate;

import lombok.Data;
import org.example.loficonnect.dto.request.estimate.EstimateUpdateRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoHighLevelEstimateUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private EstimateUpdateRequest.BusinessDetails businessDetails;
    private String currency;
    private List<EstimateUpdateRequest.Item> items;
    private Boolean liveMode;
    private EstimateUpdateRequest.Discount discount;
    private String termsNotes;
    private String title;
    private EstimateUpdateRequest.ContactDetails contactDetails;
    private Integer estimateNumber;
    private String issueDate;
    private String expiryDate;
    private EstimateUpdateRequest.SentTo sentTo;
    private Boolean automaticTaxesEnabled;
    private EstimateUpdateRequest.Meta meta;
    private EstimateUpdateRequest.SendEstimateDetails sendEstimateDetails;
    private EstimateUpdateRequest.FrequencySettings frequencySettings;
    private String estimateNumberPrefix;
    private String userId;
    private List<EstimateUpdateRequest.Attachment> attachments;
    private EstimateUpdateRequest.AutoInvoice autoInvoice;
    private String estimateStatus;

    public static GoHighLevelEstimateUpdateRequest fromRequest(EstimateUpdateRequest request) {
        GoHighLevelEstimateUpdateRequest ghlRequest = new GoHighLevelEstimateUpdateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());
        
        if (request.getBusinessDetails() != null) {
            EstimateUpdateRequest.BusinessDetails businessDetails = new EstimateUpdateRequest.BusinessDetails();
            businessDetails.setLogoUrl(request.getBusinessDetails().getLogoUrl());
            businessDetails.setName(request.getBusinessDetails().getName());
            businessDetails.setPhoneNo(request.getBusinessDetails().getPhoneNo());
            businessDetails.setAddress(request.getBusinessDetails().getAddress());
            businessDetails.setWebsite(request.getBusinessDetails().getWebsite());
            businessDetails.setCustomValues(request.getBusinessDetails().getCustomValues());
            ghlRequest.setBusinessDetails(businessDetails);
        }

        ghlRequest.setCurrency(request.getCurrency());
        
        if (request.getItems() != null) {
            List<EstimateUpdateRequest.Item> items = new ArrayList<>();
            for (EstimateUpdateRequest.Item item : request.getItems()) {
                EstimateUpdateRequest.Item mappedItem = new EstimateUpdateRequest.Item();
                mappedItem.setName(item.getName());
                mappedItem.setDescription(item.getDescription());
                mappedItem.setProductId(item.getProductId());
                mappedItem.setPriceId(item.getPriceId());
                mappedItem.setCurrency(item.getCurrency());
                mappedItem.setAmount(item.getAmount());
                mappedItem.setQty(item.getQty());
                mappedItem.setTaxes(item.getTaxes());
                mappedItem.setAutomaticTaxCategoryId(item.getAutomaticTaxCategoryId());
                mappedItem.setIsSetupFeeItem(item.getIsSetupFeeItem());
                mappedItem.setType(item.getType());
                mappedItem.setTaxInclusive(item.getTaxInclusive());
                items.add(mappedItem);
            }
            ghlRequest.setItems(items);
        }

        ghlRequest.setLiveMode(request.getLiveMode());
        
        if (request.getDiscount() != null) {
            EstimateUpdateRequest.Discount discount = new EstimateUpdateRequest.Discount();
            discount.setValue(request.getDiscount().getValue());
            discount.setType(request.getDiscount().getType());
            discount.setValidOnProductIds(request.getDiscount().getValidOnProductIds());
            ghlRequest.setDiscount(discount);
        }

        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());

        if (request.getContactDetails() != null) {
            EstimateUpdateRequest.ContactDetails contactDetails = new EstimateUpdateRequest.ContactDetails();
            contactDetails.setId(request.getContactDetails().getId());
            contactDetails.setName(request.getContactDetails().getName());
            contactDetails.setPhoneNo(request.getContactDetails().getPhoneNo());
            contactDetails.setEmail(request.getContactDetails().getEmail());
            contactDetails.setAdditionalEmails(request.getContactDetails().getAdditionalEmails());
            contactDetails.setCompanyName(request.getContactDetails().getCompanyName());
            contactDetails.setAddress(request.getContactDetails().getAddress());
            contactDetails.setCustomFields(request.getContactDetails().getCustomFields());
            ghlRequest.setContactDetails(contactDetails);
        }

        ghlRequest.setEstimateNumber(request.getEstimateNumber());
        ghlRequest.setIssueDate(request.getIssueDate());
        ghlRequest.setExpiryDate(request.getExpiryDate());

        if (request.getSentTo() != null) {
            EstimateUpdateRequest.SentTo sentTo = new EstimateUpdateRequest.SentTo();
            sentTo.setEmail(request.getSentTo().getEmail());
            sentTo.setEmailCc(request.getSentTo().getEmailCc());
            sentTo.setEmailBcc(request.getSentTo().getEmailBcc());
            sentTo.setPhoneNo(request.getSentTo().getPhoneNo());
            ghlRequest.setSentTo(sentTo);
        }

        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());

        if (request.getMeta() != null) {
            EstimateUpdateRequest.Meta meta = new EstimateUpdateRequest.Meta();
            meta.setKey(request.getMeta().getKey());
            meta.setValue(request.getMeta().getValue());
            ghlRequest.setMeta(meta);
        }

        if (request.getSendEstimateDetails() != null) {
            EstimateUpdateRequest.SendEstimateDetails sendEstimateDetails = new EstimateUpdateRequest.SendEstimateDetails();
            sendEstimateDetails.setAltId(request.getSendEstimateDetails().getAltId());
            sendEstimateDetails.setAltType(request.getSendEstimateDetails().getAltType());
            sendEstimateDetails.setAction(request.getSendEstimateDetails().getAction());
            sendEstimateDetails.setLiveMode(request.getSendEstimateDetails().getLiveMode());
            sendEstimateDetails.setUserId(request.getSendEstimateDetails().getUserId());

            EstimateUpdateRequest.SentFrom sentFrom = new EstimateUpdateRequest.SentFrom();
            sentFrom.setFromName(request.getSendEstimateDetails().getSentFrom().getFromName());
            sentFrom.setFromEmail(request.getSendEstimateDetails().getSentFrom().getFromEmail());
            sendEstimateDetails.setSentFrom(sentFrom);
            
            sendEstimateDetails.setEstimateName(request.getSendEstimateDetails().getEstimateName());
            ghlRequest.setSendEstimateDetails(sendEstimateDetails);
        }

        if (request.getFrequencySettings() != null) {
            EstimateUpdateRequest.FrequencySettings frequencySettings = new EstimateUpdateRequest.FrequencySettings();
            frequencySettings.setEnabled(request.getFrequencySettings().getEnabled());

            EstimateUpdateRequest.FrequencySettings.Schedule schedule = new EstimateUpdateRequest.FrequencySettings.Schedule();
            schedule.setExecuteAt(request.getFrequencySettings().getSchedule().getExecuteAt());
            schedule.setRrule(request.getFrequencySettings().getSchedule().getRrule());
            frequencySettings.setSchedule(schedule);
            
            ghlRequest.setFrequencySettings(frequencySettings);
        }

        ghlRequest.setEstimateNumberPrefix(request.getEstimateNumberPrefix());
        ghlRequest.setUserId(request.getUserId());

        if (request.getAttachments() != null) {
            List<EstimateUpdateRequest.Attachment> attachments = new ArrayList<>();
            for (EstimateUpdateRequest.Attachment attachment : request.getAttachments()) {
                EstimateUpdateRequest.Attachment mappedAttachment = new EstimateUpdateRequest.Attachment();
                mappedAttachment.setId(attachment.getId());
                mappedAttachment.setName(attachment.getName());
                mappedAttachment.setUrl(attachment.getUrl());
                mappedAttachment.setType(attachment.getType());
                mappedAttachment.setSize(attachment.getSize());
                attachments.add(mappedAttachment);
            }
            ghlRequest.setAttachments(attachments);
        }

        if (request.getAutoInvoice() != null) {
            EstimateUpdateRequest.AutoInvoice autoInvoice = new EstimateUpdateRequest.AutoInvoice();
            autoInvoice.setEnabled(request.getAutoInvoice().getEnabled());
            autoInvoice.setDirectPayments(request.getAutoInvoice().getDirectPayments());
            ghlRequest.setAutoInvoice(autoInvoice);
        }

        ghlRequest.setEstimateStatus(request.getEstimateStatus());

        return ghlRequest;
    }
}
