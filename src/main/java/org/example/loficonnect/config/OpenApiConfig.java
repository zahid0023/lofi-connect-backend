package org.example.loficonnect.config;

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
}
