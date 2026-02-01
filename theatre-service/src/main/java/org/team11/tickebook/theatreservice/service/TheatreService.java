package org.team11.tickebook.theatreservice.service;

import org.team11.tickebook.theatreservice.model.Theatre;

import java.util.List;
import java.util.UUID;

public interface TheatreService {
    Theatre create(Theatre theatre);
    List<Theatre> getByOwner(UUID ownerId);
    Theatre get(UUID id);
    void deactivate(UUID id);
}
