package org.example.loficonnect.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.config.GoHighLevelProperties;
import org.example.loficonnect.feignclients.GoHighLevelOAuth2Client;
import org.example.loficonnect.service.AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
    private final GoHighLevelProperties props;
    private final GoHighLevelOAuth2Client oAuth2Client;

    public AuthorizationServiceImpl(GoHighLevelProperties props, GoHighLevelOAuth2Client oauth2Client) {
        this.props = props;
        this.oAuth2Client = oauth2Client;
    }

    @Override
    public String generateAuthorizationUrl() {
        String scopesString = String.join(" ", props.getScopes());

        return UriComponentsBuilder
                .fromUriString(props.getBaseUrl())
                .pathSegment(props.getCodeUrl())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", props.getRedirectUri())
                .queryParam("client_id", props.getClientId())
                .queryParam("scope", scopesString)
                .build()
                .toUriString();

    }

    @Override
    public Map<String, Object> exchangeCodeForToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", props.getClientId());
        formData.add("client_secret", props.getClientSecret());
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", props.getRedirectUri());

        return oAuth2Client.getAccessToken(formData);
    }
}
