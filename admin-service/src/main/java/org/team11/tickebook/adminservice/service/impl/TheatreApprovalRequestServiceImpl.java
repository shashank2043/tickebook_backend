package org.team11.tickebook.adminservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.TheatreApprovalRequest;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.repository.TheatreApprovalRequestRepository;
import org.team11.tickebook.adminservice.service.TheatreApprovalRequestService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TheatreApprovalRequestServiceImpl implements TheatreApprovalRequestService {
    @Autowired
    private TheatreApprovalRequestRepository repository;
    @Autowired
    private AdminProfileRepository adminProfileRepository; // if needed

    @Override
    public TheatreApprovalResponseDto createRequest(TheatreApprovalRequestDto dto) {

        TheatreApprovalRequest request = new TheatreApprovalRequest();
        request.setTheaterId(dto.getTheatreId());
        request.setTheaterOwnerProfileId(dto.getTheatreOwnerProfileId());
        request.setStatus(ApprovalStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        TheatreApprovalRequest saved = repository.save(request);
        return mapToResponse(saved);
    }

    @Override
    public TheatreApprovalResponseDto reviewRequest(UUID id,
                                                    TheatreApprovalRequestDto dto) {

        TheatreApprovalRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        request.setStatus(dto.getStatus());
        request.setRemarks(dto.getRemarks());
        request.setReviewedAt(LocalDateTime.now());

        // Example: set admin who reviewed (mocked)
        AdminProfile admin = getCurrentAdmin();
        request.setReviewedBy(admin);

        TheatreApprovalRequest updated = repository.save(request);
        return mapToResponse(updated);
    }

    @Override
    public TheatreApprovalResponseDto getById(UUID id) {

        TheatreApprovalRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Approval request not found"));

        return mapToResponse(request);
    }

    //----------------- MAPPER -----------------

    private TheatreApprovalResponseDto mapToResponse(TheatreApprovalRequest entity) {

        TheatreApprovalResponseDto dto = new TheatreApprovalResponseDto();
        dto.setId(entity.getId());
        dto.setTheatreId(entity.getTheaterId());
        dto.setTheatreOwnerProfileId(entity.getTheaterOwnerProfileId());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setReviewedAt(entity.getReviewedAt());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getReviewedBy() != null) {
            dto.setReviewedByAdminId(entity.getReviewedBy().getId());
        }

        return dto;
    }

    // ----------------- MOCK ADMIN -----------------
    private AdminProfile getCurrentAdmin() {
        // Replace this with Spring Security context later
        return adminProfileRepository.findById(
                UUID.fromString("00000000-0000-0000-0000-000000000001")
        ).orElseThrow();
    }
}

