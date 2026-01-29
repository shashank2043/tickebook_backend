package org.team11.tickebook.adminservice.service;

import org.team11.tickebook.adminservice.dto.TheatreApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreApprovalResponseDto;

import java.util.UUID;

public interface TheatreApprovalRequestService {
    TheatreApprovalResponseDto createRequest(TheatreApprovalRequestDto dto);

    TheatreApprovalResponseDto reviewRequest(UUID id, TheatreApprovalRequestDto dto);

    TheatreApprovalResponseDto getById(UUID id);
}
