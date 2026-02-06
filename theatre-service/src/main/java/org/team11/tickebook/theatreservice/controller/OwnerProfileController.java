package org.team11.tickebook.theatreservice.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.service.OwnerProfileService;

import java.util.UUID;


@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerProfileController {

    private final OwnerProfileService service;
    @PostMapping
    public TheatreOwnerProfile create(@RequestBody TheatreOwnerProfile p) {
        return service.create(p);
    }

    @GetMapping("/{id}")
    public TheatreOwnerProfile get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping("/approval-request")
    public TheatreApprovalResponseDto approvalRequest(@RequestBody TheatreApprovalRequestDto dto){
        return service.requestTheatreApproval(dto);
    }
}
