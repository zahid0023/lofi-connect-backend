package org.example.loficonnect.dto.mapper.prices;

import lombok.Data;
import org.example.loficonnect.dto.request.prices.PriceUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelPriceUpdateRequest {
    private String name;
    private String type;
    private String currency;
    private Double amount;
    private PriceUpdateRequest.Recurring recurring;
    private String description;
    private List<PriceUpdateRequest.MembershipOffer> membershipOffers;
    private Integer trialPeriod;
    private Integer totalCycles;
    private Double setupFee;
    private List<String> variantOptionIds;
    private Double compareAtPrice;
    private String locationId;
    private String userId;
    private PriceUpdateRequest.Meta meta;
    private Boolean trackInventory;
    private Integer availableQuantity;
    private Boolean allowOutOfStockPurchases;
    private String sku;
    private PriceUpdateRequest.ShippingOptions shippingOptions;
    private Boolean isDigitalProduct;
    private List<String> digitalDelivery;

    public static GoHighLevelPriceUpdateRequest fromRequest(PriceUpdateRequest request) {
        GoHighLevelPriceUpdateRequest ghl = new GoHighLevelPriceUpdateRequest();
        ghl.setName(request.getName());
        ghl.setType(request.getType());
        ghl.setCurrency(request.getCurrency());
        ghl.setAmount(request.getAmount());
        ghl.setRecurring(request.getRecurring());
        ghl.setDescription(request.getDescription());
        ghl.setMembershipOffers(request.getMembership_offers());
        ghl.setTrialPeriod(request.getTrial_period());
        ghl.setTotalCycles(request.getTotal_cycles());
        ghl.setSetupFee(request.getSetup_fee());
        ghl.setVariantOptionIds(request.getVariant_option_ids());
        ghl.setCompareAtPrice(request.getCompare_at_price());
        ghl.setLocationId(request.getLocation_id());
        ghl.setUserId(request.getUser_id());
        ghl.setMeta(request.getMeta());
        ghl.setTrackInventory(request.getTrack_inventory());
        ghl.setAvailableQuantity(request.getAvailable_quantity());
        ghl.setAllowOutOfStockPurchases(request.getAllow_out_of_stock_purchases());
        ghl.setSku(request.getSku());
        ghl.setShippingOptions(request.getShipping_options());
        ghl.setIsDigitalProduct(request.getIs_digital_product());
        ghl.setDigitalDelivery(request.getDigital_delivery());
        return ghl;
    }
}
