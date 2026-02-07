package org.team11.tickebook.adminservice.service;

import org.team11.tickebook.adminservice.dto.RoleApprovalRequestDto;
import org.team11.tickebook.adminservice.dto.RoleApprovalResponseDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.RoleElevationRequest;

import java.util.List;
import java.util.UUID;

public interface RoleApprovalRequestService {

    Boolean createRequest(RoleApprovalRequestDto dto);

    List<RoleApprovalResponseDto> checkStatus(UUID id);

    RoleApprovalResponseDto reviewRequest(UUID id,
                                          ApprovalStatus status,
                                          String remarks,UUID reviewer);

    RoleApprovalResponseDto getById(UUID id);
    List<RoleElevationRequest> getAll();
}
