package org.team11.tickebook.auth_service.client.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.team11.tickebook.auth_service.client.AdminClient;
import org.team11.tickebook.auth_service.dto.request.RoleApprovalRequestDto;
import org.team11.tickebook.auth_service.dto.response.RoleApprovalResponseDto;

import java.util.List;
import java.util.UUID;

@Component
public class AdminClientFallback implements AdminClient {

    @Override
    public ResponseEntity<String> createRequest(RoleApprovalRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Admin service is unavailable");
    }

    @Override
    public ResponseEntity<List<RoleApprovalResponseDto>> getRequest(UUID requestedBy) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(List.of());
    }
}