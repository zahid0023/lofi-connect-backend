package org.example.loficonnect.dto.mapper.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.user.UserCreateRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelUserCreateRequest {

    @JsonAlias("company_id")
    private String companyId;

    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("last_name")
    private String lastName;

    private String email;

    @JsonAlias("pass")
    private String password;
    private String phone;
    private String type;
    private String role;

    @JsonAlias("location_ids")
    private List<String> locationIds;

    @JsonAlias("permissions")
    private Permissions permissions;

    private List<String> scopes;

    @JsonAlias("scopes_assigned_to_only")
    private List<String> scopesAssignedToOnly;

    @JsonAlias("profile_photo")
    private String profilePhoto;

    /**
     * Converts UserCreateRequest -> GoHighLevelUserCreateRequest using ObjectMapper.
     */
    public static GoHighLevelUserCreateRequest fromRequest(UserCreateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelUserCreateRequest.class);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
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