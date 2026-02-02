package org.team11.tickebook.adminservice.service;

import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;

import java.util.UUID;

public interface RoleApprovalRequestService {

    RoleApprovalResponseDto createRequest(RoleApprovalRequestDto dto);

    RoleApprovalResponseDto reviewRequest(UUID id,
                                          ApprovalStatus status,
                                          String remarks);

    RoleApprovalResponseDto getById(UUID id);
}
