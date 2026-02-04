package org.team11.tickebook.adminservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team11.tickebook.adminservice.dto.TheatreChangeRequestDto;
import org.team11.tickebook.adminservice.dto.TheatreChangeResponseDto;
import org.team11.tickebook.adminservice.model.ApprovalStatus;
import org.team11.tickebook.adminservice.model.TheatreChangeRequest;

import java.util.List;
import java.util.UUID;

public interface TheatreChangeRequestService {

    TheatreChangeResponseDto createChangeRequest(TheatreChangeRequestDto requestDto);
    List<TheatreChangeResponseDto> getAllRequests();
    TheatreChangeResponseDto approveOrReject(UUID requestedId, ApprovalStatus status,UUID adminId,String remarks);
}

