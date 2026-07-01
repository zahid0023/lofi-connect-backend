package org.example.loficonnect.subscription.dto.request.plan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.subscription.model.enums.BillingCycle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanRequest {

    @NotNull
    private Long currencyId;

    @NotNull
    private BillingCycle billingCycle;

    @Min(value = 0, message = "trial_period_days must be 0 or greater")
    private Integer trialPeriodDays = 0;

    @NotBlank
    @Size(max = 100)
    private String name;

    @DecimalMin(value = "0.00", message = "price must be 0.00 or greater")
    private BigDecimal price = BigDecimal.ZERO;

    @NotNull
    @Size(min = 1, message = "description must have at least one entry")
    private String[] description;

    @Min(value = 0, message = "sort_order must be 0 or greater")
    private Integer sortOrder = 0;

    private Boolean isPublic = Boolean.TRUE;

    /** STANDALONE (automated) or BUNDLED (manual GHL provisioning). Defaults to STANDALONE. */
    @NotNull
    private ProductType productType = ProductType.STANDALONE;

    @Valid
    private List<SubscriptionPlanLimitRequest> limits = new ArrayList<>();
}
