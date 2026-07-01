package org.example.loficonnect.subscription.serviceImpl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.exception.PaymentException;
import org.example.loficonnect.subscription.model.enums.BillingCycle;
import org.example.loficonnect.subscription.service.PaddleProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Slf4j
@Service
public class PaddleProductServiceImpl implements PaddleProductService {

    private final RestClient paddleRestClient;

    public PaddleProductServiceImpl(@Qualifier("paddleRestClient") RestClient paddleRestClient) {
        this.paddleRestClient = paddleRestClient;
    }

    @Override
    public String provisionPlan(String name, BillingCycle billingCycle, BigDecimal price,
                                String currencyCode, int trialPeriodDays) {
        try {
            // Step 1: Create Paddle product
            PaddleApiResponse<IdData> productResponse = paddleRestClient.post()
                    .uri("/products")
                    .body(new CreateProductRequest(name, "saas"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (productResponse == null || productResponse.data() == null) {
                throw new PaymentException("Paddle returned empty response when creating product for plan: " + name);
            }
            String paddleProductId = productResponse.data().id();

            // Step 2: Create Paddle price linked to that product
            String amountInCents = price.multiply(BigDecimal.valueOf(100)).toBigInteger().toString();
            BillingCycleDto billingCycleDto = toBillingCycleDto(billingCycle);
            TrialPeriodDto trialPeriodDto = trialPeriodDays > 0 ? new TrialPeriodDto("day", trialPeriodDays) : null;

            PaddleApiResponse<IdData> priceResponse = paddleRestClient.post()
                    .uri("/prices")
                    .body(new CreatePriceRequest(
                            paddleProductId,
                            name + " - " + billingCycle.name(),
                            new UnitPrice(amountInCents, currencyCode),
                            billingCycleDto,
                            trialPeriodDto
                    ))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (priceResponse == null || priceResponse.data() == null) {
                throw new PaymentException("Paddle returned empty response when creating price for plan: " + name);
            }

            String paddlePriceId = priceResponse.data().id();
            log.info("Paddle plan provisioned: productId={}, priceId={}, plan={}",
                    paddleProductId, paddlePriceId, name);
            return paddlePriceId;

        } catch (RestClientException ex) {
            log.error("Paddle API error while provisioning plan '{}': {}", name, ex.getMessage());
            throw new PaymentException("Failed to provision plan in Paddle: " + name, ex);
        }
    }

    private BillingCycleDto toBillingCycleDto(BillingCycle cycle) {
        return switch (cycle) {
            case MONTHLY   -> new BillingCycleDto("month", 1);
            case QUARTERLY -> new BillingCycleDto("month", 3);
            case ANNUAL    -> new BillingCycleDto("year", 1);
            case LIFETIME  -> null;
        };
    }

    // ─── Internal Paddle API DTOs ─────────────────────────────────────────────

    private record PaddleApiResponse<T>(T data) {}
    private record IdData(String id) {}

    private record CreateProductRequest(
            String name,
            @JsonProperty("tax_category") String taxCategory
    ) {}

    private record CreatePriceRequest(
            @JsonProperty("product_id") String productId,
            String description,
            @JsonProperty("unit_price") UnitPrice unitPrice,
            @JsonProperty("billing_cycle") BillingCycleDto billingCycle,
            @JsonProperty("trial_period") TrialPeriodDto trialPeriod
    ) {}

    private record UnitPrice(
            String amount,
            @JsonProperty("currency_code") String currencyCode
    ) {}

    private record BillingCycleDto(String interval, int frequency) {}
    private record TrialPeriodDto(String interval, int frequency) {}
}
