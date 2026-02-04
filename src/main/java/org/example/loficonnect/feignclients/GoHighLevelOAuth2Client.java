package org.example.loficonnect.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "gohighlevelOAuth2", url = "${GHL_TOKEN_URL}")
public interface GoHighLevelOAuth2Client {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getAccessToken(@RequestBody MultiValueMap<String, String> formData);
}

