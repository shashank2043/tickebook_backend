package org.team11.tickebook.theatreservice.service;

import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.model.Theatre;

import java.util.List;
import java.util.UUID;

public interface TheatreService {
    Theatre create(Theatre theatre, Authentication authentication);

    List<Theatre> getMyTheatres(Authentication authentication);

    void deactivate(UUID id, Authentication authentication);
    Theatre get(UUID id);

}
