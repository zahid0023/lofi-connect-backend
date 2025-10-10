package org.example.loficonnect.dto.mapper.invoicetemplate;

import lombok.Data;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateCreateRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoHighLevelInvoiceTemplateCreateRequest {
    private String altId;
    private String altType;
    private Boolean internal;
    private String name;
    private BusinessDetails businessDetails;
    private String currency;
    private List<InvoiceItem> items;
    private Boolean automaticTaxesEnabled;
    private Discount discount;
    private String termsNotes;
    private String title;
    private TipsConfiguration tipsConfiguration;
    private LateFeesConfiguration lateFeesConfiguration;
    private String invoiceNumberPrefix;
    private PaymentMethods paymentMethods;
    private List<String> attachments;

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

    @Data
    public static class TipsConfiguration {
        private List<Integer> tipsPercentage;
        private Boolean tipsEnabled;
    }

    @Data
    public static class LateFeesConfiguration {
        private Boolean enable;
        private Double value;
        private String type;
        private Frequency frequency;
        private Frequency grace;
        private MaxLateFees maxLateFees;
    }

    @Data
    public static class Frequency {
        private Integer intervalCount;
        private String interval;
    }

    @Data
    public static class MaxLateFees {
        private String type;
        private String value;
    }

    @Data
    public static class PaymentMethods {
        private Stripe stripe;
    }

    @Data
    public static class Stripe {
        private Boolean enableBankDebitOnly;
    }

    public static GoHighLevelInvoiceTemplateCreateRequest fromRequest(InvoiceTemplateCreateRequest request) {
        GoHighLevelInvoiceTemplateCreateRequest ghlRequest = new GoHighLevelInvoiceTemplateCreateRequest();

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
        for (InvoiceTemplateCreateRequest.InvoiceItem item : request.getItems()) {
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
            for (InvoiceTemplateCreateRequest.Tax tax : item.getTaxes()) {
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

        TipsConfiguration tipsConfiguration = new TipsConfiguration();
        tipsConfiguration.setTipsPercentage(request.getTipsConfiguration().getTipsPercentage());
        tipsConfiguration.setTipsEnabled(request.getTipsConfiguration().getTipsEnabled());
        ghlRequest.setTipsConfiguration(tipsConfiguration);

        LateFeesConfiguration lateFeesConfiguration = new LateFeesConfiguration();
        lateFeesConfiguration.setEnable(request.getLateFeesConfiguration().getEnable());
        lateFeesConfiguration.setValue(request.getLateFeesConfiguration().getValue());
        lateFeesConfiguration.setType(request.getLateFeesConfiguration().getType());

        Frequency frequency = new Frequency();
        frequency.setIntervalCount(request.getLateFeesConfiguration().getFrequency().getIntervalCount());
        frequency.setInterval(request.getLateFeesConfiguration().getFrequency().getInterval());
        lateFeesConfiguration.setFrequency(frequency);

        Frequency grace = new Frequency();
        grace.setIntervalCount(request.getLateFeesConfiguration().getGrace().getIntervalCount());
        grace.setInterval(request.getLateFeesConfiguration().getGrace().getInterval());
        lateFeesConfiguration.setGrace(grace);

        MaxLateFees maxLateFees = new MaxLateFees();
        maxLateFees.setType(request.getLateFeesConfiguration().getMaxLateFees().getType());
        maxLateFees.setValue(request.getLateFeesConfiguration().getMaxLateFees().getValue());
        lateFeesConfiguration.setMaxLateFees(maxLateFees);

        ghlRequest.setLateFeesConfiguration(lateFeesConfiguration);

        PaymentMethods paymentMethods = new PaymentMethods();
        Stripe stripe = new Stripe();
        stripe.setEnableBankDebitOnly(request.getPaymentMethods().getStripe().getEnableBankDebitOnly());
        paymentMethods.setStripe(stripe);
        ghlRequest.setPaymentMethods(paymentMethods);

        ghlRequest.setAttachments(request.getAttachments());

        return ghlRequest;
    }

}
