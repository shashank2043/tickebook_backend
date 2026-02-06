package org.team11.tickebook.theatreservice.service;

import jakarta.validation.Valid;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;

import java.util.UUID;

public interface OwnerProfileService {
    TheatreOwnerProfile create(TheatreOwnerProfile profile);
    TheatreOwnerProfile get(UUID id);
    TheatreOwnerProfile update(TheatreOwnerProfile profile);
    TheatreApprovalResponseDto requestTheatreApproval(@Valid TheatreApprovalRequestDto request);
}
