package org.example.loficonnect.dto.mapper.estimate;

import lombok.Data;
import org.example.loficonnect.dto.request.estimate.EstimateCreateRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoHighLevelEstimateCreateRequest {
    private String altId;
    private String altType;
    private String name;
    private EstimateCreateRequest.BusinessDetails businessDetails;
    private String currency;
    private List<EstimateCreateRequest.Item> items;
    private Boolean liveMode;
    private EstimateCreateRequest.Discount discount;
    private String termsNotes;
    private String title;
    private EstimateCreateRequest.ContactDetails contactDetails;
    private Integer estimateNumber;
    private String issueDate;
    private String expiryDate;
    private EstimateCreateRequest.SentTo sentTo;
    private Boolean automaticTaxesEnabled;
    private EstimateCreateRequest.Meta meta;
    private EstimateCreateRequest.SendEstimateDetails sendEstimateDetails;
    private EstimateCreateRequest.FrequencySettings frequencySettings;
    private String estimateNumberPrefix;
    private String userId;
    private List<EstimateCreateRequest.Attachment> attachments;
    private EstimateCreateRequest.AutoInvoice autoInvoice;

    public static GoHighLevelEstimateCreateRequest fromRequest(EstimateCreateRequest request) {
        GoHighLevelEstimateCreateRequest ghlRequest = new GoHighLevelEstimateCreateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setName(request.getName());

        if (request.getBusinessDetails() != null) {
            EstimateCreateRequest.BusinessDetails businessDetails = new EstimateCreateRequest.BusinessDetails();
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
            List<EstimateCreateRequest.Item> items = new ArrayList<>();
            for (EstimateCreateRequest.Item item : request.getItems()) {
                EstimateCreateRequest.Item mappedItem = new EstimateCreateRequest.Item();
                mappedItem.setName(item.getName());
                mappedItem.setDescription(item.getDescription());
                mappedItem.setProductId(item.getProductId());
                mappedItem.setPriceId(item.getPriceId());
                mappedItem.setCurrency(item.getCurrency());
                mappedItem.setAmount(item.getAmount());
                mappedItem.setQty(item.getQty());
                mappedItem.setTaxes(item.getTaxes()); // Assuming the taxes are directly passed
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
            EstimateCreateRequest.Discount discount = new EstimateCreateRequest.Discount();
            discount.setValue(request.getDiscount().getValue());
            discount.setType(request.getDiscount().getType());
            discount.setValidOnProductIds(request.getDiscount().getValidOnProductIds());
            ghlRequest.setDiscount(discount);
        }

        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());

        if (request.getContactDetails() != null) {
            EstimateCreateRequest.ContactDetails contactDetails = new EstimateCreateRequest.ContactDetails();
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
            EstimateCreateRequest.SentTo sentTo = new EstimateCreateRequest.SentTo();
            sentTo.setEmail(request.getSentTo().getEmail());
            sentTo.setEmailCc(request.getSentTo().getEmailCc());
            sentTo.setEmailBcc(request.getSentTo().getEmailBcc());
            sentTo.setPhoneNo(request.getSentTo().getPhoneNo());
            ghlRequest.setSentTo(sentTo);
        }

        ghlRequest.setAutomaticTaxesEnabled(request.getAutomaticTaxesEnabled());

        if (request.getMeta() != null) {
            EstimateCreateRequest.Meta meta = new EstimateCreateRequest.Meta();
            meta.setKey(request.getMeta().getKey());
            meta.setValue(request.getMeta().getValue());
            ghlRequest.setMeta(meta);
        }

        if (request.getSendEstimateDetails() != null) {
            EstimateCreateRequest.SendEstimateDetails sendEstimateDetails = new EstimateCreateRequest.SendEstimateDetails();
            sendEstimateDetails.setAltId(request.getSendEstimateDetails().getAltId());
            sendEstimateDetails.setAltType(request.getSendEstimateDetails().getAltType());
            sendEstimateDetails.setAction(request.getSendEstimateDetails().getAction());
            sendEstimateDetails.setLiveMode(request.getSendEstimateDetails().getLiveMode());
            sendEstimateDetails.setUserId(request.getSendEstimateDetails().getUserId());

            EstimateCreateRequest.SentFrom sentFrom = new EstimateCreateRequest.SentFrom();
            sentFrom.setFromName(request.getSendEstimateDetails().getSentFrom().getFromName());
            sentFrom.setFromEmail(request.getSendEstimateDetails().getSentFrom().getFromEmail());
            sendEstimateDetails.setSentFrom(sentFrom);

            sendEstimateDetails.setEstimateName(request.getSendEstimateDetails().getEstimateName());
            ghlRequest.setSendEstimateDetails(sendEstimateDetails);
        }

        if (request.getFrequencySettings() != null) {
            EstimateCreateRequest.FrequencySettings frequencySettings = new EstimateCreateRequest.FrequencySettings();
            frequencySettings.setEnabled(request.getFrequencySettings().getEnabled());

            EstimateCreateRequest.FrequencySettings.Schedule schedule = new EstimateCreateRequest.FrequencySettings.Schedule();
            schedule.setExecuteAt(request.getFrequencySettings().getSchedule().getExecuteAt());
            schedule.setRrule(request.getFrequencySettings().getSchedule().getRrule());
            frequencySettings.setSchedule(schedule);

            ghlRequest.setFrequencySettings(frequencySettings);
        }

        ghlRequest.setEstimateNumberPrefix(request.getEstimateNumberPrefix());
        ghlRequest.setUserId(request.getUserId());

        if (request.getAttachments() != null) {
            List<EstimateCreateRequest.Attachment> attachments = new ArrayList<>();
            for (EstimateCreateRequest.Attachment attachment : request.getAttachments()) {
                EstimateCreateRequest.Attachment mappedAttachment = new EstimateCreateRequest.Attachment();
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
            EstimateCreateRequest.AutoInvoice autoInvoice = new EstimateCreateRequest.AutoInvoice();
            autoInvoice.setEnabled(request.getAutoInvoice().getEnabled());
            autoInvoice.setDirectPayments(request.getAutoInvoice().getDirectPayments());
            ghlRequest.setAutoInvoice(autoInvoice);
        }

        return ghlRequest;
    }
}

