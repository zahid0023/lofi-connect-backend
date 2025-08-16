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
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", props.getClientId());
        formData.add("client_secret", props.getClientSecret());
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", props.getRedirectUri());

        return oAuth2Client.getAccessToken(formData);
    }

    @Override
    @Transactional
    public AppKeyResponse generateAndSaveAppKey(Map<String, Object> parameters) {
        String appKey = secretKeyService.generateSecretKey();
        LofiConnectAppKeyEntity lofiConnectAppKeyEntity = saveAppKey(appKey, parameters.get("locationId").toString());
        saveGoHighLevelTokens(lofiConnectAppKeyEntity, parameters);
        return new AppKeyResponse(appKey);
    }

    private LofiConnectAppKeyEntity saveAppKey(String appKey, String locationId) {
        // Find and deactivate all active app keys for this location
        List<LofiConnectAppKeyEntity> appKeyEntities = lofiConnectAppKeyRepository.findAllActiveForLocationId(locationId).stream()
                .peek(appKeyEntity -> appKeyEntity.setIsActive(false))
                .toList();

        lofiConnectAppKeyRepository.saveAll(appKeyEntities);

        LofiConnectAppKeyEntity lofiConnectAppKeyEntity = LofiConnectAppKeyEntity.from(appKey);
        return lofiConnectAppKeyRepository.save(lofiConnectAppKeyEntity);
    }

    private void saveGoHighLevelTokens(LofiConnectAppKeyEntity lofiConnectAppKeyEntity, Map<String, Object> parameters) {
        GoHighLevelTokenEntity goHighLevelTokenEntity = GoHighLevelTokenEntity.from(lofiConnectAppKeyEntity, parameters);
        goHighLevelTokenRepository.save(goHighLevelTokenEntity);
    }

    @Override
    public String getAccessToken(String appKey) {
        LofiConnectAppKeyEntity appKeyEntity = lofiConnectAppKeyRepository.findByAppKey(appKey).orElseThrow(() -> new EntityNotFoundException("App key not found"));
        GoHighLevelTokenEntity goHighLevelTokenEntity = goHighLevelTokenRepository.findFirstByAppKeyEntity(appKeyEntity).orElseThrow(() -> new EntityNotFoundException("Active token not found"));
        goHighLevelTokenEntity = isAccessTokenValid(goHighLevelTokenEntity) ? goHighLevelTokenEntity : null;
        log.info("Access token: {}", goHighLevelTokenEntity.getAccessToken());
        return "Bearer " + goHighLevelTokenEntity.getAccessToken();
    }


    private boolean isAccessTokenValid(GoHighLevelTokenEntity token) {
        Instant createdAt = token.getCreatedAt();

        if (createdAt == null) {
            return false; // no creation time → invalid
        }

        // calculate how long ago the token was created
        Duration age = Duration.between(createdAt, Instant.now());

        // valid if token is less than 23 hours 50 minutes old
        return age.toMinutes() < (23 * 60 + 50);
    }
}
