package org.team11.tickebook.adminservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;
import org.team11.tickebook.adminservice.service.TheatreApprovalRequestService;

import java.util.UUID;

@RestController
@RequestMapping("/api/theatre-approval-requests")
public class TheatreApprovalRequestController {
	@Autowired
    private TheatreApprovalRequestService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TheatreApprovalResponseDto> createRequest(
            @RequestBody TheatreApprovalRequestDto requestDto
    ) {
        return ResponseEntity.ok(service.createRequest(requestDto));
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<TheatreApprovalResponseDto> reviewRequest(
            @PathVariable UUID id,
            @RequestBody TheatreApprovalRequestDto requestDto
    ) {
        return ResponseEntity.ok(service.reviewRequest(id, requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreApprovalResponseDto> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }
}
