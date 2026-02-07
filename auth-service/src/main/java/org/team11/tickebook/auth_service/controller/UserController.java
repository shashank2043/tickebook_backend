package org.team11.tickebook.auth_service.controller;

import java.util.List;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.auth_service.client.AdminClient;
import org.team11.tickebook.auth_service.dto.OtpRequest;
import org.team11.tickebook.auth_service.dto.request.RoleApprovalRequestDto;
import org.team11.tickebook.auth_service.dto.response.RoleApprovalResponseDto;
import org.team11.tickebook.auth_service.security.CustomUserDetails;
import org.team11.tickebook.auth_service.service.UserService;
import org.team11.tickebook.auth_service.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService service;
    @Autowired
    private AdminClient adminClient;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUser(id));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> generateOtp(Authentication authentication) {
        String email = authentication.getName(); // comes from JWT
        System.out.println(email);
        service.generateOtp(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Otp sent to the email address: " + email);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            Authentication authentication,
            @RequestBody OtpRequest req) {
        String email = authentication.getName();
        service.validateOtp(email, req.getOtp());
        return ResponseEntity.ok("Otp verified");
    }

    @PostMapping("/role-elevation")
    public ResponseEntity<?> roleElevationRequest(
            Authentication authentication,
            @RequestBody RoleApprovalRequestDto dto
    ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        UUID userId = user.getId();

        dto.setRequestedBy(userId);

        ResponseEntity<String> response = adminClient.createRequest(dto);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody());
    }

    @GetMapping("/role-elevation")
    public ResponseEntity<?> checkRoleElevationStatus(Authentication authentication) {


        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        UUID userId = user.getId();

        ResponseEntity<List<RoleApprovalResponseDto>> response =
                adminClient.getRequest(userId);

        return ResponseEntity.ok(response.getBody());
    }
}
