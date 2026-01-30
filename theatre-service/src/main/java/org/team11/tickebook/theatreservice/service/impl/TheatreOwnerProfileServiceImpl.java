package org.team11.tickebook.theatreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.team11.tickebook.theatreservice.dto.request.TheatreOwnerProfileRequest;
import org.team11.tickebook.theatreservice.dto.response.TheatreOwnerProfileResponse;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.service.TheatreOwnerProfileService;
@RequiredArgsConstructor
public class TheatreOwnerProfileServiceImpl implements TheatreOwnerProfileService {

    private final TheatreOwnerProfileRepository profileRepository;
    @Override
    public TheatreOwnerProfileResponse registerTheatreOwnerProfile(TheatreOwnerProfileRequest request) {
        TheatreOwnerProfile theatreOwnerProfile = new TheatreOwnerProfile();
        theatreOwnerProfile.setUserId(request.getUserId());
        theatreOwnerProfile.setTheatres(request.getTheatres());
        theatreOwnerProfile.setBusinessAddress(request.getBusinessAddress());
        theatreOwnerProfile.setBusinessName(request.getBusinessName());
        theatreOwnerProfile.setBusinessEmail(request.getBusinessEmail());
        TheatreOwnerProfile saved = profileRepository.save(theatreOwnerProfile);
        return TheatreOwnerProfileResponse
                .builder()
                .businessName(saved.getBusinessName())
                .theatres(saved.getTheatres())
                .businessEmail(saved.getBusinessEmail())
                .businessAddress(saved.getBusinessAddress()).build();
    }
}
