package org.example.loficonnect.dto.mapper.invoicetemplate;

import lombok.Data;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateUpdateRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoHighLevelInvoiceTemplateUpdateRequest {
    private String altId;
    private String altType;
    private Boolean internal;
    private String name;
    private BusinessDetails businessDetails;
    private String currency;
    private List<InvoiceItem> items;
    private Discount discount;
    private String termsNotes;
    private String title;

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
    public static class InvoiceItem {
        private String name;
        private String description;
        private String productId;
        private String priceId;
        private String currency;
        private Double amount;
        private Integer qty;
        private List<Tax> taxes;
        private String automaticTaxCategoryId;
        private Boolean isSetupFeeItem;
        private String type;
        private Boolean taxInclusive;
    }

    @Data
    public static class Tax {
        private String _id;
        private String name;
        private Double rate;
        private String calculation;
        private String description;
        private String taxId;
    }

    @Data
    public static class Discount {
        private Double value;
        private String type;
        private List<String> validOnProductIds;
    }

    public static GoHighLevelInvoiceTemplateUpdateRequest fromRequest(InvoiceTemplateUpdateRequest request) {
        GoHighLevelInvoiceTemplateUpdateRequest ghlRequest = new GoHighLevelInvoiceTemplateUpdateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setInternal(request.getInternal());
        ghlRequest.setName(request.getName());

        BusinessDetails businessDetails = new BusinessDetails();
        businessDetails.setLogoUrl(request.getBusinessDetails().getLogoUrl());
        businessDetails.setName(request.getBusinessDetails().getName());
        businessDetails.setPhoneNo(request.getBusinessDetails().getPhoneNo());
        businessDetails.setAddress(request.getBusinessDetails().getAddress());
        businessDetails.setWebsite(request.getBusinessDetails().getWebsite());
        businessDetails.setCustomValues(request.getBusinessDetails().getCustomValues());
        ghlRequest.setBusinessDetails(businessDetails);

        ghlRequest.setCurrency(request.getCurrency());

        List<InvoiceItem> ghlItems = new ArrayList<>();
        for (InvoiceTemplateUpdateRequest.InvoiceItem item : request.getItems()) {
            InvoiceItem ghlItem = new InvoiceItem();
            ghlItem.setName(item.getName());
            ghlItem.setDescription(item.getDescription());
            ghlItem.setProductId(item.getProductId());
            ghlItem.setPriceId(item.getPriceId());
            ghlItem.setCurrency(item.getCurrency());
            ghlItem.setAmount(item.getAmount());
            ghlItem.setQty(item.getQty());
            ghlItem.setAutomaticTaxCategoryId(item.getAutomaticTaxCategoryId());
            ghlItem.setIsSetupFeeItem(item.getIsSetupFeeItem());
            ghlItem.setType(item.getType());
            ghlItem.setTaxInclusive(item.getTaxInclusive());

            List<Tax> taxes = new ArrayList<>();
            for (InvoiceTemplateUpdateRequest.Tax tax : item.getTaxes()) {
                Tax ghlTax = new Tax();
                ghlTax.set_id(tax.get_id());
                ghlTax.setName(tax.getName());
                ghlTax.setRate(tax.getRate());
                ghlTax.setCalculation(tax.getCalculation());
                ghlTax.setDescription(tax.getDescription());
                ghlTax.setTaxId(tax.getTaxId());
                taxes.add(ghlTax);
            }
            ghlItem.setTaxes(taxes);

            ghlItems.add(ghlItem);
        }
        ghlRequest.setItems(ghlItems);

        Discount discount = new Discount();
        discount.setValue(request.getDiscount().getValue());
        discount.setType(request.getDiscount().getType());
        discount.setValidOnProductIds(request.getDiscount().getValidOnProductIds());
        ghlRequest.setDiscount(discount);

        ghlRequest.setTermsNotes(request.getTermsNotes());
        ghlRequest.setTitle(request.getTitle());

        return ghlRequest;
    }

}
