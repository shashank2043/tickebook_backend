package org.team11.tickebook.adminservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.dto.RoleReviewRequestDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.Role;
import org.team11.tickebook.adminservice.security.SecurityUtil;
import org.team11.tickebook.adminservice.service.RoleApprovalRequestService;

import java.util.UUID;

@RestController
@RequestMapping("/api/role-approval-requests")
@RequiredArgsConstructor
public class RoleApprovalRequestController {

    private final RoleApprovalRequestService roleApprovalRequestService;
    private final SecurityUtil securityUtil;
    // Create role change request


    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleApprovalResponseDto> reviewRequest(
            @PathVariable UUID id,
            @RequestBody RoleReviewRequestDto dto,
            Authentication authentication
    ) {
        UUID reviewerId = securityUtil.getUserId(authentication);

        return ResponseEntity.ok(
                roleApprovalRequestService.reviewRequest(
                        id,
                        dto.getStatus(),
                        dto.getRemarks(),
                        reviewerId
                )
        );
    }


    // Get request by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleApprovalResponseDto> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                roleApprovalRequestService.getById(id)
        );
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRequests(){
        return ResponseEntity.ok(roleApprovalRequestService.getAll());
    }
}
