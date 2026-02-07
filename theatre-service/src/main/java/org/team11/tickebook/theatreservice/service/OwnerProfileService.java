package org.team11.tickebook.theatreservice.service;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.dto.request.TheatreApprovalRequestDto;
import org.team11.tickebook.theatreservice.dto.response.TheatreApprovalResponseDto;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;

import java.util.List;
import java.util.UUID;

public interface OwnerProfileService {
    TheatreOwnerProfile create(TheatreOwnerProfile profile);
    TheatreOwnerProfile getByUserId(UUID userId);
    TheatreOwnerProfile update(TheatreOwnerProfile profile);
    Boolean requestTheatreApproval(Authentication authentication, @Valid TheatreApprovalRequestDto request);
    List<TheatreApprovalResponseDto> checkStatus(UUID id, Authentication authentication);
    void verifyOwner(UUID ownerProfileId);
}
