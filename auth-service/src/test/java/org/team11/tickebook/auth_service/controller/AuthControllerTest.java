package org.team11.tickebook.auth_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.team11.tickebook.auth_service.dto.request.LoginRequest;
import org.team11.tickebook.auth_service.dto.request.RegistrationRequest;
import org.team11.tickebook.auth_service.dto.response.LoginResponse;
import org.team11.tickebook.auth_service.dto.response.RegistrationResponse;
import org.team11.tickebook.auth_service.dto.response.RegistrationResult;
import org.team11.tickebook.auth_service.model.entity.User;
import org.team11.tickebook.auth_service.model.enums.Role;
import org.team11.tickebook.auth_service.security.CustomUserDetailsService;
import org.team11.tickebook.auth_service.security.JwtUtil;
import org.team11.tickebook.auth_service.service.AuthService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

    private LoginRequest loginRequest;
    private RegistrationRequest registrationRequest;
    private User user;

    @BeforeEach
    void setup() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("password");

        registrationRequest = new RegistrationRequest(
                "test@mail.com", "password", "Test", "User"
        );

        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@mail.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRoles(List.of(Role.USER));
        user.setEmailVerified(false);
    }

    // ================= LOGIN =================

    @Test
    void login_shouldAuthenticateAndReturnTokenAndCookie_whenValidCredentials() {

        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername(loginRequest.getEmail()))
                .thenReturn(userDetails);

        when(jwtUtil.generateToken(userDetails))
                .thenReturn("jwt-token");

        when(jwtUtil.extraxtUserId("jwt-token"))
                .thenReturn(user.getId().toString());

        ResponseEntity<?> response = controller.login(loginRequest);

        // ✅ Status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // ✅ Cookie header
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertTrue(cookie.contains("accessToken=jwt-token"));

        // ✅ Body
        LoginResponse body = (LoginResponse) response.getBody();
        assertNotNull(body);
        assertEquals("jwt-token", body.getToken());
        assertEquals(user.getId().toString(), body.getUserId());
        assertTrue(body.isSuccess());
        assertEquals("Login Successful!", body.getMessage());

        // ✅ Verify interactions
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(loginRequest.getEmail());
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void login_shouldThrowException_whenAuthenticationFails() {

        doThrow(new RuntimeException("Invalid credentials"))
                .when(authenticationManager).authenticate(any());

        assertThrows(RuntimeException.class,
                () -> controller.login(loginRequest));

        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    // ================= REGISTER =================

    @Test
    void register_shouldReturnResponseAndCookie_whenValidRequest() {

        RegistrationResult result = new RegistrationResult(user, "jwt-token");

        when(authService.register(registrationRequest)).thenReturn(result);

        ResponseEntity<RegistrationResponse> response =
                controller.register(registrationRequest);

        // ✅ Status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // ✅ Cookie
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertTrue(cookie.contains("accessToken=jwt-token"));

        // ✅ Body
        RegistrationResponse body = response.getBody();
        assertNotNull(body);

        assertEquals(user.getId(), body.getId());
        assertEquals(user.getEmail(), body.getEmail());
        assertEquals(user.getFirstName(), body.getFirstName());
        assertEquals(user.getLastName(), body.getLastName());
        assertEquals(user.getRoles().toString(), body.getRole());
        assertFalse(body.isEmailVerified());

        verify(authService).register(registrationRequest);
    }

    @Test
    void register_shouldThrowException_whenServiceFails() {

        when(authService.register(registrationRequest))
                .thenThrow(new RuntimeException("Registration failed"));

        assertThrows(RuntimeException.class,
                () -> controller.register(registrationRequest));

        verify(authService).register(registrationRequest);
    }

    // ================= LOGOUT =================

    @Test
    void logout_shouldClearCookie_andReturnMessage() {

        ResponseEntity<?> response = controller.logout();

        // ✅ Status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // ✅ Cookie cleared
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertTrue(cookie.contains("accessToken="));
        assertTrue(cookie.contains("Max-Age=0"));

        // ✅ Body
        assertEquals("Logged out", response.getBody());
    }
}