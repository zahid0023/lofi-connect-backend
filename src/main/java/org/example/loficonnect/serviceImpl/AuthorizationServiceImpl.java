package org.example.loficonnect.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.config.GoHighLevelProperties;
import org.example.loficonnect.dto.response.AppKeyResponse;
import org.example.loficonnect.feignclients.GoHighLevelOAuth2Client;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.model.entity.LofiConnectAppKeyEntity;
import org.example.loficonnect.repository.GoHighLevelTokenRepository;
import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.SecretKeyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
    private static final String GRANT_TYPE_AUTH_CODE = "authorization_code";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";
    private static final long TOKEN_VALIDITY_MINUTES = (23 * 60) + 50; // 23h 50m

    private final GoHighLevelProperties props;
    private final GoHighLevelOAuth2Client oAuth2Client;
    private final GoHighLevelTokenRepository goHighLevelTokenRepository;
    private final LofiConnectAppKeyRepository lofiConnectAppKeyRepository;
    private final SecretKeyService secretKeyService;

    public AuthorizationServiceImpl(GoHighLevelProperties props,
                                    GoHighLevelOAuth2Client oauth2Client,
                                    GoHighLevelTokenRepository goHighLevelTokenRepository,
                                    LofiConnectAppKeyRepository lofiConnectAppKeyRepository,
                                    SecretKeyService secretKeyService) {
        this.props = props;
        this.oAuth2Client = oauth2Client;
        this.goHighLevelTokenRepository = goHighLevelTokenRepository;
        this.lofiConnectAppKeyRepository = lofiConnectAppKeyRepository;
        this.secretKeyService = secretKeyService;
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
        return oAuth2Client.getAccessToken(buildTokenRequest(GRANT_TYPE_AUTH_CODE,
                Map.of("code", code)));
    }

    @Override
    @Transactional
    public AppKeyResponse generateAndSaveAppKey(Map<String, Object> parameters) {
        final String appKey = secretKeyService.generateSecretKey();
        final String locationId = parameters.get("locationId").toString();

        // deactivate old keys
        deactivateOldKeys(locationId);

        // create new app key + tokens
        LofiConnectAppKeyEntity savedAppKey = lofiConnectAppKeyRepository.save(LofiConnectAppKeyEntity.from(appKey));
        saveGoHighLevelTokenEntity(savedAppKey, parameters);

        return new AppKeyResponse(appKey);
    }

    private GoHighLevelTokenEntity saveGoHighLevelTokenEntity(LofiConnectAppKeyEntity savedAppKey, Map<String, Object> parameters) {
        return goHighLevelTokenRepository.save(GoHighLevelTokenEntity.from(savedAppKey, parameters));
    }

    @Transactional
    @Override
    public String getAccessToken(String appKey) {
        LofiConnectAppKeyEntity appKeyEntity = lofiConnectAppKeyRepository.findByAppKey(appKey).orElseThrow(() -> new EntityNotFoundException("App key not found"));

        GoHighLevelTokenEntity goHighLevelTokenEntity = goHighLevelTokenRepository.findFirstByAppKeyEntityAndIsActive(appKeyEntity, true).orElseThrow(() -> new EntityNotFoundException("Active token not found"));

        if (!isAccessTokenValid(goHighLevelTokenEntity)) {
            log.info("Access token is not valid");
            log.info("Access token is generating from refresh token");
            log.info(appKey);
            Map<String, Object> parameters = refreshAccessToken(goHighLevelTokenEntity.getRefreshToken());
            goHighLevelTokenEntity.setIsActive(false);
            goHighLevelTokenRepository.save(goHighLevelTokenEntity);
            goHighLevelTokenEntity = saveGoHighLevelTokenEntity(appKeyEntity, parameters);
            log.info("Refreshed access token for appKey {}: {}", appKey, parameters);
        }

        return "Bearer " + goHighLevelTokenEntity.getAccessToken();
    }

    @Override
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        return oAuth2Client.getAccessToken(buildTokenRequest(GRANT_TYPE_REFRESH,
                Map.of("refresh_token", refreshToken)));
    }

    // -------------------- Private Helpers --------------------

    private MultiValueMap<String, String> buildTokenRequest(String grantType, Map<String, String> extraParams) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", props.getClientId());
        formData.add("client_secret", props.getClientSecret());
        formData.add("grant_type", grantType);
        formData.add("redirect_uri", props.getRedirectUri());

        extraParams.forEach(formData::add);
        return formData;
    }

    private void deactivateOldKeys(String locationId) {
        List<LofiConnectAppKeyEntity> activeKeys = lofiConnectAppKeyRepository.findAllActiveForLocationId(locationId);
        activeKeys.forEach(k -> k.setIsActive(false));
        lofiConnectAppKeyRepository.saveAll(activeKeys);
    }

    private boolean isAccessTokenValid(GoHighLevelTokenEntity token) {
        Instant createdAt = token.getCreatedAt();
        if (createdAt == null) return false;

        return Duration.between(createdAt, Instant.now()).toMinutes() < TOKEN_VALIDITY_MINUTES;
    }
}
