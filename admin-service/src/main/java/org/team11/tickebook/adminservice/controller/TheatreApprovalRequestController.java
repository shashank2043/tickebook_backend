package org.team11.tickebook.adminservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalReviewDto;
import org.team11.tickebook.adminservice.model.TheatreApprovalRequest;
import org.team11.tickebook.adminservice.service.TheatreApprovalRequestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/theatre-approval-requests")
public class TheatreApprovalRequestController {
    @Autowired
    private TheatreApprovalRequestService service;

    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheatreApprovalResponseDto> reviewRequest(
            @PathVariable UUID id,
            @RequestBody TheatreApprovalReviewDto requestDto,
            Authentication authentication
    ) {
        return ResponseEntity.ok(service.reviewRequest(id, requestDto, authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreApprovalResponseDto> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TheatreApprovalResponseDto>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }
}
