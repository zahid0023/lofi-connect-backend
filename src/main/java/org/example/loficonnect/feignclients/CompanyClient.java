package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "companyClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CompanyClient {
    @GetMapping(
            value = "/companies/{companyId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCompany(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("companyId") String companyId
    );

}
