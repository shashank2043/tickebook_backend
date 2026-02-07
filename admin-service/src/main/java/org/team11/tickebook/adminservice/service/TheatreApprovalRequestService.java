package org.team11.tickebook.adminservice.service;

import org.springframework.security.core.Authentication;
import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalReviewDto;

import java.util.List;
import java.util.UUID;

public interface TheatreApprovalRequestService {
    Boolean createRequest(TheatreApprovalRequestDto dto);

    TheatreApprovalResponseDto reviewRequest(UUID id, TheatreApprovalReviewDto dto, Authentication authentication);

    TheatreApprovalResponseDto getById(UUID id);

    List<TheatreApprovalResponseDto> checkStatus(UUID requestedBy);

    List<TheatreApprovalResponseDto> getAll();
}
