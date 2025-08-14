package org.example.loficonnect.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "gohighlevel-oauth2", url = "${gohighlevel.token-url}")
public interface GoHighLevelOAuth2Client {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getAccessToken(@RequestBody MultiValueMap<String, String> formData);
}

