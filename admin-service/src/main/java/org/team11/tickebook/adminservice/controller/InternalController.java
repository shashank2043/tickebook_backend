package org.team11.tickebook.adminservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;
import org.team11.tickebook.adminservice.service.RoleApprovalRequestService;
import org.team11.tickebook.adminservice.service.TheatreApprovalRequestService;

import java.util.UUID;

@RestController
@RequestMapping("/internal")
public class InternalController {
    @Autowired
    private TheatreApprovalRequestService service;
    @Autowired
    private RoleApprovalRequestService roleApprovalRequestService;

    @PostMapping("/approval-request")
    public ResponseEntity<TheatreApprovalResponseDto> createRequest(
            @RequestBody TheatreApprovalRequestDto requestDto
    ) {
        return ResponseEntity.ok(service.createRequest(requestDto));
    }

    @PostMapping("/role-elevation")
    public ResponseEntity<?> createRequest(
            @RequestBody RoleApprovalRequestDto requestDto
    ) {
        Boolean result = roleApprovalRequestService.createRequest(requestDto);
        if (result) return ResponseEntity.ok("Role Elevation request is placed successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid role elevation request");
    }
    @GetMapping("/role-elevation")
    public ResponseEntity<?> getRequests(@RequestParam UUID requestedBy){
        return ResponseEntity.ok(roleApprovalRequestService.checkStatus(requestedBy));
    }
}
