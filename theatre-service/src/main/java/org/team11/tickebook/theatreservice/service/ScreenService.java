package org.team11.tickebook.theatreservice.service;

import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.dto.request.CreateScreenRequest;
import org.team11.tickebook.theatreservice.model.Screen;

import java.util.List;
import java.util.UUID;

public interface ScreenService {
    Screen create(CreateScreenRequest screen, Authentication authentication);
    List<Screen> getByTheatre(UUID theatreId, Authentication authentication);
}
