package org.team11.tickebook.adminservice.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.adminservice.client.AuthClient;
import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.RoleElevationRequest;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.repository.RoleElevationRequestRepository;
import org.team11.tickebook.adminservice.service.RoleApprovalRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleApprovalRequestServiceImpl
        implements RoleApprovalRequestService {

    @Autowired
    private RoleElevationRequestRepository repository;
    @Autowired
    private AdminProfileRepository adminProfileRepository;
    @Autowired
    private AuthClient authClient;
    @Override
    public Boolean createRequest(RoleApprovalRequestDto dto) {
        RoleElevationRequest request = new RoleElevationRequest();
        request.setRequestedBy(dto.getRequestedBy());
        request.setCurrentRole(dto.getCurrentRole());
        request.setRequestedRole(dto.getRequestedRole());
        request.setDescription(dto.getDescription());
        request.setStatus(ApprovalStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        repository.save(request);
        return true;
    }

    @Override
    public List<RoleApprovalResponseDto> checkStatus(UUID requestedBy) {
        List<RoleElevationRequest> byRequestedBy = repository.findByRequestedBy(requestedBy);
        return byRequestedBy.stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public RoleApprovalResponseDto reviewRequest(UUID id,
                                                 ApprovalStatus status,
                                                 String remarks, UUID reviewer) {

        RoleElevationRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(status);
        request.setRemarks(remarks);
        request.setReviewedAt(LocalDateTime.now());
        request.setReviewedBy(reviewer);

        // If approved → update admin role
        if (status == ApprovalStatus.APPROVED) {
            authClient.updateUserRole(
                    request.getRequestedBy(),
                    request.getRequestedRole()
            );
        }

        return mapToResponse(repository.save(request));
    }

    @Override
    public RoleApprovalResponseDto getById(UUID id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @Override
    public List<RoleElevationRequest> getAll() {
        return repository.findAll();
    }

    // ---------------- MAPPER ----------------

    private RoleApprovalResponseDto mapToResponse(RoleElevationRequest entity) {

        RoleApprovalResponseDto dto = new RoleApprovalResponseDto();
        dto.setId(entity.getId());
        dto.setRequestedBy(entity.getRequestedBy());
        dto.setCurrentRole(entity.getCurrentRole());
        dto.setRequestedRole(entity.getRequestedRole());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setReviewedAt(entity.getReviewedAt());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}