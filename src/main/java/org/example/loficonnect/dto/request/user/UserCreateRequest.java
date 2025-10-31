package org.example.loficonnect.dto.request.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCreateRequest {
    private String companyId;
    private String firstName;
    private String lastName;
    private String email;
    private String pass;
    private String phone;
    private String type;
    private String role;
    private List<String> locationIds;
    private Permissions permissions; // nested permissions object
    private List<String> scopes;
    private List<String> scopesAssignedToOnly;
    private String profilePhoto;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Permissions {
        private Boolean campaignsEnabled;
        private Boolean campaignsReadOnly;
        private Boolean contactsEnabled;
        private Boolean workflowsEnabled;
        private Boolean workflowsReadOnly;
        private Boolean triggersEnabled;
        private Boolean funnelsEnabled;
        private Boolean websitesEnabled;
        private Boolean opportunitiesEnabled;
        private Boolean dashboardStatsEnabled;
        private Boolean bulkRequestsEnabled;
        private Boolean appointmentsEnabled;
        private Boolean reviewsEnabled;
        private Boolean onlineListingsEnabled;
        private Boolean phoneCallEnabled;
        private Boolean conversationsEnabled;
        private Boolean assignedDataOnly;
        private Boolean adwordsReportingEnabled;
        private Boolean membershipEnabled;
        private Boolean facebookAdsReportingEnabled;
        private Boolean attributionsReportingEnabled;
        private Boolean settingsEnabled;
        private Boolean tagsEnabled;
        private Boolean leadValueEnabled;
        private Boolean marketingEnabled;
        private Boolean agentReportingEnabled;
        private Boolean botService;
        private Boolean socialPlanner;
        private Boolean bloggingEnabled;
        private Boolean invoiceEnabled;
        private Boolean affiliateManagerEnabled;
        private Boolean contentAiEnabled;
        private Boolean refundsEnabled;
        private Boolean recordPaymentEnabled;
        private Boolean cancelSubscriptionEnabled;
        private Boolean paymentsEnabled;
        private Boolean communitiesEnabled;
        private Boolean exportPaymentsEnabled;
    }
}