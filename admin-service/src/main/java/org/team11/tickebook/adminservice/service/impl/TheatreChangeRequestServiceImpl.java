package org.team11.tickebook.adminservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team11.tickebook.adminservice.dto.TheatreChangeRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreChangeResponseDto;
import org.team11.tickebook.adminservice.model.AdminProfile;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.TheatreChangeRequest;
import org.team11.tickebook.adminservice.repository.AdminProfileRepository;
import org.team11.tickebook.adminservice.repository.TheatreChangeRequestRepository;
import org.team11.tickebook.adminservice.service.TheatreChangeRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TheatreChangeRequestServiceImpl
        implements TheatreChangeRequestService {
    @Autowired
    private  TheatreChangeRequestRepository repository;
    @Autowired
    private  AdminProfileRepository adminProfileRepository;

    @Override
    public TheatreChangeResponseDto createChangeRequest(
            TheatreChangeRequestDto dto) {

        TheatreChangeRequest entity = new TheatreChangeRequest();
        entity.setTheaterId(dto.getTheatreId());
        entity.setChangeType(dto.getChangeType());
        entity.setOldValue(dto.getOldValue());
        entity.setNewValue(dto.getNewValue());
        entity.setRequestedBy(dto.getRequestedBy());
        entity.setStatus(ApprovalStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());

        return mapToResponse(repository.save(entity));
    }

    @Override
    public List<TheatreChangeResponseDto> getAllRequests() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TheatreChangeResponseDto approveOrReject(
            UUID requestId,
            ApprovalStatus status,
            UUID adminId,
            String remarks) {

        TheatreChangeRequest request = repository.findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException("Change request not found"));

        AdminProfile admin = adminProfileRepository.findById(adminId)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        request.setStatus(status);
        request.setReviewedBy(admin);
        request.setReviewedAt(LocalDateTime.now());
        request.setRemarks(remarks);

        return mapToResponse(repository.save(request));
    }

    private TheatreChangeResponseDto mapToResponse(
            TheatreChangeRequest entity) {

        return TheatreChangeResponseDto.builder()
                .id(entity.getId())
                .theatreId(entity.getTheaterId())
                .changeType(entity.getChangeType())
                .oldValue(entity.getOldValue())
                .newValue(entity.getNewValue())
                .status(entity.getStatus())
                .requestedBy(entity.getRequestedBy())
                .reviewedBy(
                        entity.getReviewedBy() != null
                                ? entity.getReviewedBy().getId()
                                : null
                )
                .reviewedAt(entity.getReviewedAt())
                .remarks(entity.getRemarks())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

