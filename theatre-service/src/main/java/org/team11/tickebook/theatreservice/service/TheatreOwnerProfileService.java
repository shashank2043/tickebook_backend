package org.team11.tickebook.theatreservice.service;

import jakarta.validation.Valid;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.request.TheatreOwnerProfileRequest;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreOwnerProfileResponse;

public interface TheatreOwnerProfileService {
    public TheatreOwnerProfileResponse registerTheatreOwnerProfile(TheatreOwnerProfileRequest request);

    TheatreApprovalResponseDto requestTheatreApproval(@Valid TheatreApprovalRequestDto request);
}
