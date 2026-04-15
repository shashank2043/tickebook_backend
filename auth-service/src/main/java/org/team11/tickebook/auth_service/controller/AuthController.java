package org.team11.tickebook.auth_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team11.tickebook.auth_service.dto.request.LoginRequest;
import org.team11.tickebook.auth_service.dto.request.RegistrationRequest;
import org.team11.tickebook.auth_service.dto.response.LoginResponse;
import org.team11.tickebook.auth_service.dto.response.RegistrationResponse;
import org.team11.tickebook.auth_service.dto.response.RegistrationResult;
import org.team11.tickebook.auth_service.security.CustomUserDetailsService;
import org.team11.tickebook.auth_service.security.JwtUtil;
import org.team11.tickebook.auth_service.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;
    private static final String COOKIE_NAME = "accessToken";
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtUtil.generateToken(userDetails);
        
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)        // 🔐 JS cannot access
                .secure(false)         // true in production (HTTPS)
                .path("/")
                .maxAge(60L * 60)       // 1 hour
                .sameSite("Strict")    // CSRF protection
                .build();
        LoginResponse loginResponse = LoginResponse.builder()
                .userId(jwtUtil.extraxtUserId(token))
                .token(token).success(true)
                .message("Login Successful!")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest request) {
        RegistrationResult result = authService.register(request);
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, result.getToken())
                .httpOnly(true)
                .secure(false)     // true in prod
                .path("/")
                .maxAge(60L * 60)
                .sameSite("Lax")
                .build();
        RegistrationResponse response =
                new RegistrationResponse(
                        result.getUser().getId(),
                        result.getUser().getEmail(),
                        result.getUser().getFirstName(),
                        result.getUser().getLastName(),
                        result.getUser().getRoles().toString(),
                        result.getUser().isEmailVerified()
                );
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out");
    }
}
