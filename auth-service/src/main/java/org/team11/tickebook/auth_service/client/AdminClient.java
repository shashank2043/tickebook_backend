package org.team11.tickebook.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.team11.tickebook.auth_service.dto.request.RoleApprovalRequestDto;
import org.team11.tickebook.auth_service.dto.response.RoleApprovalResponseDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ADMIN-SERVICE")
public interface AdminClient {
    @PostMapping("/internal/role-elevation")
    ResponseEntity<String> createRequest(
            @RequestBody RoleApprovalRequestDto requestDto
    );

    @GetMapping("/internal/role-elevation")
    ResponseEntity<List<RoleApprovalResponseDto>> getRequest(
            @RequestParam UUID requestedBy
    );
}
