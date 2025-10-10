package org.example.loficonnect.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.feignclients.BusinessClient;
import org.example.loficonnect.dto.request.businesses.BusinessCreateRequest;
import org.example.loficonnect.dto.request.businesses.BusinessUpdateRequest;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.serviceImpl.BusinessServiceImpl;
import org.example.loficonnect.util.AppKeyContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceImplTest {

    //Mock Objects
    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private BusinessClient businessClient;

    @InjectMocks
    private BusinessServiceImpl businessServiceImpl;

    @BeforeEach
    void setUp() {
        // This method will run before each test to set up mocks and the service
    }

    @Test
    void testGetBusiness() {
        // Arrange
        String businessId = "123";
        JsonNode mockResponse = mock(JsonNode.class);
        when(authorizationService.getAccessToken(AppKeyContext.getAppKey())).thenReturn("mockAccessToken");
        when(businessClient.getBusiness(any(), any(), eq(businessId))).thenReturn(mockResponse);

        // Act
        JsonNode result = businessServiceImpl.getBusiness(businessId);

        // Assert
        assertNotNull(result);
        verify(authorizationService, times(1)).getAccessToken(AppKeyContext.getAppKey());
        verify(businessClient, times(1)).getBusiness(any(), any(), eq(businessId));
    }

    @Test
    void testCreateBusiness() {
        // Arrange
        BusinessCreateRequest request = new BusinessCreateRequest();
        JsonNode mockResponse = mock(JsonNode.class);
        when(authorizationService.getAccessToken(AppKeyContext.getAppKey())).thenReturn("mockAccessToken");
        when(businessClient.createBusiness(any(), any(), any())).thenReturn(mockResponse);

        // Act
        JsonNode result = businessServiceImpl.createBusiness(request);

        // Assert
        assertNotNull(result);  // Ensure that the result is not null
        verify(authorizationService, times(1)).getAccessToken(AppKeyContext.getAppKey());
        verify(businessClient, times(1)).createBusiness(any(), any(), any());
    }

    @Test
    void testUpdateBusiness() {
        // Arrange
        String businessId = "123";
        BusinessUpdateRequest request = new BusinessUpdateRequest();
        JsonNode mockResponse = mock(JsonNode.class);
        when(authorizationService.getAccessToken(AppKeyContext.getAppKey())).thenReturn("mockAccessToken");
        when(businessClient.updateBusiness(any(), any(), eq(businessId), any())).thenReturn(mockResponse);

        // Act
        JsonNode result = businessServiceImpl.updateBusiness(businessId, request);

        // Assert
        assertNotNull(result);
        verify(authorizationService, times(1)).getAccessToken(AppKeyContext.getAppKey());
        verify(businessClient, times(1)).updateBusiness(any(), any(), eq(businessId), any());
    }

    @Test
    void testGetBusinessesByLocation() {
        // Arrange
        String locationId = "location123";
        JsonNode mockResponse = mock(JsonNode.class);
        when(authorizationService.getAccessToken(AppKeyContext.getAppKey())).thenReturn("mockAccessToken");
        when(businessClient.getBusinessesByLocation(any(), any(), any())).thenReturn(mockResponse);

        // Act
        JsonNode result = businessServiceImpl.getBusinessesByLocation(Map.of("locationId", locationId));

        // Assert
        assertNotNull(result);
        verify(authorizationService, times(1)).getAccessToken(AppKeyContext.getAppKey());
        verify(businessClient, times(1)).getBusinessesByLocation(any(), any(), any());
    }

    @Test
    void testDeleteBusiness() {
        // Arrange
        String businessId = "123";
        JsonNode mockResponse = mock(JsonNode.class);
        when(authorizationService.getAccessToken(AppKeyContext.getAppKey())).thenReturn("mockAccessToken");
        when(businessClient.deleteBusiness(any(), any(), eq(businessId))).thenReturn(mockResponse);

        // Act
        JsonNode result = businessServiceImpl.deleteBusiness(businessId);

        // Assert
        assertNotNull(result);
        verify(authorizationService, times(1)).getAccessToken(AppKeyContext.getAppKey());
        verify(businessClient, times(1)).deleteBusiness(any(), any(), eq(businessId));
    }
}