package org.example.loficonnect.commons.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // format is optional
                        ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    public GroupedOpenApi authenticationApi() {
        return GroupedOpenApi.builder()
                .group("Authentication APIs") // dropdown name in Swagger UI
                .pathsToMatch("/api/v1/auth/**") // your path pattern
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Authentication API Documentation")
                        .description("Endpoints for managing Authentication")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin APIs") // dropdown name in Swagger UI
                .pathsToMatch("/api/v1/admins/**") // your path pattern
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Admin API Documentation")
                        .description("Endpoints for managing Admin")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlContactsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Contacts APIs") // dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/contacts/**") // your path pattern
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Contacts API Documentation")
                        .description("Endpoints for managing GHL Contacts")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlOpportunitiesApi() {
        return GroupedOpenApi.builder()
                .group("GHL Opportunities APIs") // ✅ dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/opportunities/**") // ✅ correct path
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Opportunities API Documentation") // ✅ correct title
                        .description("Endpoints for managing GHL Opportunities") // ✅ correct description
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlLocationsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Locations APIs") // ✅ dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/locations/**") // ✅ correct path
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Locations API Documentation") // ✅ correct title
                        .description("Endpoints for managing GHL Locations") // ✅ correct description
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlCalendarsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Calendar APIs") // dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/calendars/**") // your path pattern
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Calendar API Documentation")
                        .description("Endpoints for managing GHL Calendars")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlUsersApi() {
        return GroupedOpenApi.builder()
                .group("GHL Users APIs") // ✅ dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/users/**") // ✅ correct path
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Users API Documentation") // ✅ correct title
                        .description("Endpoints for managing GHL Users") // ✅ correct description
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlWorkflowsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Workflows APIs") // ✅ dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/workflows/**") // ✅ correct path
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Workflows API Documentation") // ✅ correct title
                        .description("Endpoints for managing GHL Workflows") // ✅ correct description
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlCustomFieldsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Custom Fields APIs") // dropdown name in Swagger UI
                .pathsToMatch("/api/v1/ghl/custom-fields/**") // your path pattern
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Custom Fields API Documentation")
                        .description("Endpoints for managing GHL Custom Fields")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlAuthorizationApi() {
        return GroupedOpenApi.builder()
                .group("Authorization APIs") // ✅ dropdown name in Swagger UI
                .pathsToMatch("/api/v1/authorization/**") // ✅ correct path
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Authorizations API Documentation") // ✅ correct title
                        .description("Endpoints for managing Authorizations") // ✅ correct description
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlBusinessesApi() {
        return GroupedOpenApi.builder()
                .group("GHL Businesses APIs")
                .pathsToMatch("/api/v1/ghl/businesses/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Businesses API Documentation")
                        .description("Endpoints for managing GHL Businesses")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlCampaignsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Campaigns APIs")
                .pathsToMatch("/api/v1/ghl/campaigns/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Campaigns API Documentation")
                        .description("Endpoints for managing GHL Campaigns")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlCompaniesApi() {
        return GroupedOpenApi.builder()
                .group("GHL Companies APIs")
                .pathsToMatch("/api/v1/ghl/companies/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Companies API Documentation")
                        .description("Endpoints for managing GHL Companies")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlObjectsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Objects APIs")
                .pathsToMatch("/api/v1/ghl/objects/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Objects API Documentation")
                        .description("Endpoints for managing GHL Objects")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlAssociationsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Associations APIs")
                .pathsToMatch("/api/v1/ghl/associations/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Associations API Documentation")
                        .description("Endpoints for managing GHL Associations")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlConversationsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Conversations APIs")
                .pathsToMatch("/api/v1/ghl/conversations/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Conversations API Documentation")
                        .description("Endpoints for managing GHL Conversations")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlCoursesApi() {
        return GroupedOpenApi.builder()
                .group("GHL Courses APIs")
                .pathsToMatch("/api/v1/ghl/courses/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Courses API Documentation")
                        .description("Endpoints for managing GHL Courses")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlEmailApi() {
        return GroupedOpenApi.builder()
                .group("GHL Email APIs")
                .pathsToMatch("/api/v1/ghl/emails/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Email API Documentation")
                        .description("Endpoints for managing GHL Email")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlFormsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Forms APIs")
                .pathsToMatch("/api/v1/ghl/forms/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Forms API Documentation")
                        .description("Endpoints for managing GHL Forms")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlInvoiceApi() {
        return GroupedOpenApi.builder()
                .group("GHL Invoice APIs")
                .pathsToMatch("/api/v1/ghl/invoices/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Invoice API Documentation")
                        .description("Endpoints for managing GHL Invoice")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlTriggerLinkApi() {
        return GroupedOpenApi.builder()
                .group("GHL Trigger Link APIs")
                .pathsToMatch("/api/v1/ghl/trigger/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Trigger Link API Documentation")
                        .description("Endpoints for managing GHL Trigger Link")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlMediaStorageApi() {
        return GroupedOpenApi.builder()
                .group("GHL Media Storage APIs")
                .pathsToMatch("/api/v1/ghl/medias/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Media Storage API Documentation")
                        .description("Endpoints for managing GHL Media Storage")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlMarketplaceApi() {
        return GroupedOpenApi.builder()
                .group("GHL Marketplace APIs")
                .pathsToMatch("/api/v1/ghl/marketplace/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Marketplace API Documentation")
                        .description("Endpoints for managing GHL Marketplace")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlBlogsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Blogs APIs")
                .pathsToMatch("/api/v1/ghl/blogs/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Blogs API Documentation")
                        .description("Endpoints for managing GHL Blogs")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlFunnelsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Funnels APIs")
                .pathsToMatch("/api/v1/ghl/funnels/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Funnels API Documentation")
                        .description("Endpoints for managing GHL Funnels")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlPaymentsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Payments APIs")
                .pathsToMatch("/api/v1/ghl/payments/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Payments API Documentation")
                        .description("Endpoints for managing GHL Payments")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlProductsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Products APIs")
                .pathsToMatch("/api/v1/ghl/products/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Products API Documentation")
                        .description("Endpoints for managing GHL Products")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlSaasApi() {
        return GroupedOpenApi.builder()
                .group("GHL Products APIs")
                .pathsToMatch("/api/v1/ghl/saas/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Saas API Documentation")
                        .description("Endpoints for managing GHL Saas")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlSnapshotsApi() {
        return GroupedOpenApi.builder()
                .group("GHL Snapshots APIs")
                .pathsToMatch("/api/v1/ghl/snapshots/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Snapshots API Documentation")
                        .description("Endpoints for managing GHL Snapshots")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlSocialPlannerApi() {
        return GroupedOpenApi.builder()
                .group("GHL Social Planner APIs")
                .pathsToMatch("/api/v1/ghl/oauth/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Social Planner API Documentation")
                        .description("Endpoints for managing GHL Social Planner")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlSurveysApi() {
        return GroupedOpenApi.builder()
                .group("GHL Surveys APIs")
                .pathsToMatch("/api/v1/ghl/surveys/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Surveys API Documentation")
                        .description("Endpoints for managing GHL Surveys")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlCustomMenusApi() {
        return GroupedOpenApi.builder()
                .group("GHL Custom Menus APIs")
                .pathsToMatch("/api/v1/ghl/custom-menu-links/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Custom Menus API Documentation")
                        .description("Endpoints for managing GHL Custom Menus")
                        .version("v1")))
                .build();
    }

    @Bean
    public GroupedOpenApi ghlStoreApi() {
        return GroupedOpenApi.builder()
                .group("GHL Store APIs")
                .pathsToMatch("/api/v1/ghl/store/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("GHL Store API Documentation")
                        .description("Endpoints for managing GHL Store")
                        .version("v1")))
                .build();
    }

}
