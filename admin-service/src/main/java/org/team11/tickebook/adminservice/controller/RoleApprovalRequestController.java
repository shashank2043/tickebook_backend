package org.team11.tickebook.adminservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.Role;
import org.team11.tickebook.adminservice.service.RoleApprovalRequestService;

import java.util.UUID;

@RestController
@RequestMapping("/api/role-approval-requests")
@RequiredArgsConstructor
public class RoleApprovalRequestController {

    private RoleApprovalRequestService roleApprovalRequestService;

    // Create role change request
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleApprovalResponseDto> createRequest(
            @RequestBody RoleApprovalRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                roleApprovalRequestService.createRequest(requestDto)
        );
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reviewRequest(
            @PathVariable UUID id,
            @RequestParam ApprovalStatus status,
            @RequestParam(required = false) String remarks,
            @RequestParam UUID reviewed
    ) {
        return ResponseEntity.ok(
                roleApprovalRequestService.reviewRequest(id, status, remarks,reviewed)
        );
    }

    // Get request by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleApprovalResponseDto> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                roleApprovalRequestService.getById(id)
        );
    }
}
