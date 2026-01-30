package org.team11.tickebook.theatreservice.service;

import org.team11.tickebook.theatreservice.dto.request.TheatreOwnerProfileRequest;
import org.team11.tickebook.theatreservice.dto.response.TheatreOwnerProfileResponse;

public interface TheatreOwnerProfileService {
    public TheatreOwnerProfileResponse registerTheatreOwnerProfile(TheatreOwnerProfileRequest request);
}
