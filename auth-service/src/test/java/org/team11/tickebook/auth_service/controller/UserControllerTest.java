package org.team11.tickebook.auth_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.auth_service.client.AdminClient;
import org.team11.tickebook.auth_service.dto.OtpRequest;
import org.team11.tickebook.auth_service.dto.request.RoleApprovalRequestDto;
import org.team11.tickebook.auth_service.dto.response.RoleApprovalResponseDto;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.security.CustomUserDetails;
import org.team11.tickebook.auth_service.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @Mock
    private AdminClient adminClient;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController controller;

    private UUID userId;
    private User user;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail("test@mail.com");

        customUserDetails = mock(CustomUserDetails.class);
    }

    // ================= GET USER =================

    @Test
    void getUser_shouldReturnUser_whenValidId() {
        when(service.getUser(userId)).thenReturn(user);

        ResponseEntity<?> response = controller.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(service).getUser(userId);
    }

    // ================= GET MY PROFILE =================

    @Test
    void getMyProfile_shouldReturnUser_whenAuthenticated() {
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getId()).thenReturn(userId);
        when(service.getUser(userId)).thenReturn(user);

        ResponseEntity<?> response = controller.getMyProfile(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(service).getUser(userId);
    }

    // ================= GENERATE OTP =================

    @Test
    void generateOtp_shouldSendOtp_andReturnCreatedStatus() {
        when(authentication.getName()).thenReturn("test@mail.com");

        ResponseEntity<?> response = controller.generateOtp(authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Otp sent"));

        verify(service).generateOtp("test@mail.com");
    }

    // ================= VERIFY OTP =================

    @Test
    void verifyOtp_shouldValidateOtp_andReturnSuccess() {
        when(authentication.getName()).thenReturn("test@mail.com");

        OtpRequest request = new OtpRequest();
        request.setOtp("123456");

        ResponseEntity<?> response =
                controller.verifyOtp(authentication, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Otp verified", response.getBody());

        verify(service).validateOtp("test@mail.com", "123456");
    }

    @Test
    void verifyOtp_shouldHandleNullOtp() {
        when(authentication.getName()).thenReturn("test@mail.com");

        OtpRequest request = new OtpRequest(); // otp = null

        ResponseEntity<?> response =
                controller.verifyOtp(authentication, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service).validateOtp("test@mail.com", null);
    }

    // ================= ROLE ELEVATION REQUEST =================

    @Test
    void roleElevationRequest_shouldForwardRequest_andReturnAdminResponse() {
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getId()).thenReturn(userId);

        RoleApprovalRequestDto dto = new RoleApprovalRequestDto();

        when(adminClient.createRequest(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Request sent"));

        ResponseEntity<?> response =
                controller.roleElevationRequest(authentication, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Request sent", response.getBody());

        verify(adminClient).createRequest(dto);
        assertEquals(userId, dto.getRequestedBy());
    }

    // ================= CHECK ROLE STATUS =================

    @Test
    void checkRoleElevationStatus_shouldReturnList_whenValidUser() {
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getId()).thenReturn(userId);

        List<RoleApprovalResponseDto> list =
                List.of(new RoleApprovalResponseDto());

        when(adminClient.getRequest(userId))
                .thenReturn(ResponseEntity.ok(list));

        ResponseEntity<?> response =
                controller.checkRoleElevationStatus(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());

        verify(adminClient).getRequest(userId);
    }

    // ================= EDGE CASE =================

    @Test
    void getMyProfile_shouldThrowException_whenPrincipalInvalid() {
        when(authentication.getPrincipal()).thenReturn("invalid");

        assertThrows(ClassCastException.class,
                () -> controller.getMyProfile(authentication));
    }
}