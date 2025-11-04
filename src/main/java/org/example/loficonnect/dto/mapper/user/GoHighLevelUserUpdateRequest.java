package org.example.loficonnect.dto.mapper.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.user.UserUpdateRequest;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelUserUpdateRequest {

    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("last_name")
    private String lastName;

    private String email;

    @JsonAlias("email_change_otp")
    private String emailChangeOTP;

    private String password;
    private String phone;

    @JsonAlias("is_ejected_user")
    private Boolean isEjectedUser;

    private String type;
    private String role;

    @JsonAlias("company_id")
    private String companyId;

    @JsonAlias("location_ids")
    private List<String> locationIds;

    private Map<String, Boolean> permissions;
    private List<String> scopes;

    @JsonAlias("scopes_assigned_to_only")
    private List<String> scopesAssignedToOnly;

    @JsonAlias("profile_photo")
    private String profilePhoto;

    /**
     * Converts UserUpdateRequest -> GoHighLevelUserUpdateRequest using ObjectMapper.
     */
    public static GoHighLevelUserUpdateRequest fromRequest(UserUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelUserUpdateRequest.class);
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