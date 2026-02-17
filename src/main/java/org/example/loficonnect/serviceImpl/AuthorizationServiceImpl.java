package org.example.loficonnect.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.config.GoHighLevelProperties;
import org.example.loficonnect.feignclients.GoHighLevelOAuth2Client;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.model.mapper.GoHighLevelTokenMapper;
import org.example.loficonnect.repository.GoHighLevelTokenRepository;
import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.LocationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public AuthorizationServiceImpl(GoHighLevelProperties props,
                                    GoHighLevelOAuth2Client oauth2Client,
                                    GoHighLevelTokenRepository goHighLevelTokenRepository,
                                    LofiConnectAppKeyRepository lofiConnectAppKeyRepository) {
        this.props = props;
        this.oAuth2Client = oauth2Client;
        this.goHighLevelTokenRepository = goHighLevelTokenRepository;
        this.lofiConnectAppKeyRepository = lofiConnectAppKeyRepository;
    }

    @Override
    public String generateAuthorizationUrl(List<String> scopes, Long apiKeyId) {
        String scopesString = String.join(" ", scopes);

        return UriComponentsBuilder
                .fromUriString(props.getBaseUrl())
                .path(props.getCodeUrl())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", props.getRedirectUri())
                .queryParam("client_id", props.getClientId())
                .queryParam("scope", scopesString)
                .queryParam("state", apiKeyId)
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
    public void saveGoHighLevelToken(LofiConnectAppKeyEntity entity, Map<String, Object> parameters) {
        // deactivate all previous tokens for this app key
        Set<GoHighLevelTokenEntity> entities = entity.getGoHighLevelTokens().stream()
                .peek(goHighLevelTokenEntity -> goHighLevelTokenEntity.setIsActive(false))
                .collect(Collectors.toSet());
        goHighLevelTokenRepository.saveAll(entities);

        // save a new token for this app key
        saveGoHighLevelTokenEntity(entity, parameters);
    }

    private GoHighLevelTokenEntity saveGoHighLevelTokenEntity(LofiConnectAppKeyEntity savedAppKey, Map<String, Object> parameters) {
        return goHighLevelTokenRepository.save(GoHighLevelTokenMapper.toEntity(
                savedAppKey,
                parameters.get("access_token").toString(),
                parameters.get("token_type").toString(),
                (Integer) parameters.get("expires_in"),
                parameters.get("refresh_token").toString(),
                parameters.get("refreshTokenId").toString(),
                parameters.get("companyId").toString(),
                parameters.get("locationId").toString(),
                "",
                parameters.get("scope").toString(),
                parameters.get("userType").toString(),
                parameters.get("userId").toString()
        ));
    }

    @Transactional
    @Override
    public String getAccessToken(String appKey) {
        LofiConnectAppKeyEntity appKeyEntity = lofiConnectAppKeyRepository.findByAppKeyAndIsActiveAndIsDeleted(appKey, true, false).orElseThrow(() -> new EntityNotFoundException("App key not found"));

        GoHighLevelTokenEntity goHighLevelTokenEntity = goHighLevelTokenRepository.findByAppKeyEntityAndIsActiveAndIsDeleted(appKeyEntity, true, false)
                .orElseThrow(() -> new EntityNotFoundException("GoHighLevelTokenEntity not found"));

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

        LocationContext.setLocationId(goHighLevelTokenEntity.getLocationId());

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

    private boolean isAccessTokenValid(GoHighLevelTokenEntity token) {
        Instant createdAt = Instant.now();
        if (createdAt == null) return false;

        return Duration.between(createdAt, Instant.now()).toMinutes() < TOKEN_VALIDITY_MINUTES;
    }
}
