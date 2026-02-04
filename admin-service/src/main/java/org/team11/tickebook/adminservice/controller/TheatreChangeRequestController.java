package org.team11.tickebook.adminservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.adminservice.dto.TheatreChangeRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreChangeResponseDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.service.impl.TheatreChangeRequestServiceImpl;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/theatre-change-requests")

public class TheatreChangeRequestController {

    @Autowired
    private TheatreChangeRequestServiceImpl theatreChangeRequestService;

    @PostMapping
    public ResponseEntity<TheatreChangeResponseDto> createChangeRequest(@RequestBody TheatreChangeRequestDto requestDto){
        return ResponseEntity.ok().body(theatreChangeRequestService.createChangeRequest(requestDto));
    }
    @GetMapping
    public ResponseEntity<List<TheatreChangeResponseDto>> getAll() {
        return ResponseEntity.ok(theatreChangeRequestService.getAllRequests());
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<TheatreChangeResponseDto> review(
            @PathVariable UUID id,
            @RequestParam ApprovalStatus status,
            @RequestParam UUID adminId,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                theatreChangeRequestService.approveOrReject(id, status, adminId, remarks));
    }


}
