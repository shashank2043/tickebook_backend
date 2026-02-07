package org.team11.tickebook.theatreservice.controller;

import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerProfileController {

    private final OwnerProfileService service;
    @PostMapping
    public TheatreOwnerProfile create(
            Authentication authentication,
            @RequestBody TheatreOwnerProfile p
    ) {
        Claims claims = (Claims) authentication.getPrincipal();

        UUID userId = UUID.fromString(claims.get("userId", String.class));

        p.setUserId(userId);
        return service.create(p);
    }

    @GetMapping("/me")
    public TheatreOwnerProfile get(Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();

        UUID userId = UUID.fromString(claims.get("userId", String.class));


        return service.getByUserId(userId);
    }

    @PostMapping("/approval-request")
    public ResponseEntity<?> approvalRequest(@RequestBody TheatreApprovalRequestDto dto,Authentication authentication){
        Boolean response = service.requestTheatreApproval(authentication,dto);
        if(response) return ResponseEntity.ok("Theatre Approval Request Placed successfully");
        return ResponseEntity.badRequest().body("Unable to place Theatre Approval Request");
    }
    @GetMapping("/approval-request")
    public ResponseEntity<?> checkStatus(@RequestParam UUID theatreId,Authentication authentication){
        List<TheatreApprovalResponseDto> response =
                service.checkStatus(theatreId, authentication);

        return ResponseEntity.ok(response);
    }
}
