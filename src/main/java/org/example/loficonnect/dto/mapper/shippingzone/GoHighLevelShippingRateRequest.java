package org.example.loficonnect.dto.mapper.shippingzone;

import lombok.Data;
import org.example.loficonnect.dto.request.shippingzone.ShippingRateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelShippingRateRequest {
    private String altId;
    private String altType;
    private String country;
    private Address address;
    private String amountAvailable;
    private Double totalOrderAmount;
    private Boolean weightAvailable;
    private Double totalOrderWeight;
    private Source source;
    private List<Product> products;
    private String couponCode;

    @Data
    public static class Address {
        private String name;
        private String companyName;
        private String addressLine1;
        private String country;
        private String state;
        private String city;
        private String zip;
        private String phone;
        private String email;
    }

    @Data
    public static class Source {
        private String type;
        private String subType;
    }

    @Data
    public static class Product {
        private String id;
        private Integer qty;
    }

    public static GoHighLevelShippingRateRequest fromRequest(ShippingRateRequest request) {
        GoHighLevelShippingRateRequest ghl = new GoHighLevelShippingRateRequest();
        ghl.setAltId(request.getAltId());
        ghl.setAltType(request.getAltType());
        ghl.setCountry(request.getCountry());
        ghl.setAmountAvailable(request.getAmountAvailable());
        ghl.setTotalOrderAmount(request.getTotalOrderAmount());
        ghl.setWeightAvailable(request.getWeightAvailable());
        ghl.setTotalOrderWeight(request.getTotalOrderWeight());
        ghl.setCouponCode(request.getCouponCode());

        if (request.getAddress() != null) {
            Address a = new Address();
            a.setName(request.getAddress().getName());
            a.setCompanyName(request.getAddress().getCompanyName());
            a.setAddressLine1(request.getAddress().getAddressLine1());
            a.setCountry(request.getAddress().getCountry());
            a.setState(request.getAddress().getState());
            a.setCity(request.getAddress().getCity());
            a.setZip(request.getAddress().getZip());
            a.setPhone(request.getAddress().getPhone());
            a.setEmail(request.getAddress().getEmail());
            ghl.setAddress(a);
        }

        if (request.getSource() != null) {
            Source s = new Source();
            s.setType(request.getSource().getType());
            s.setSubType(request.getSource().getSubType());
            ghl.setSource(s);
        }

        if (request.getProducts() != null) {
            ghl.setProducts(
                request.getProducts().stream().map(p -> {
                    Product product = new Product();
                    product.setId(p.getId());
                    product.setQty(p.getQty());
                    return product;
                }).collect(Collectors.toList())
            );
        }

        return ghl;
    }
}
