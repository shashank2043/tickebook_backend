package org.team11.tickebook.theatreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.exception.TheatreNotFoundException;
import org.team11.tickebook.theatreservice.model.Theatre;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.repository.TheatreRepository;
import org.team11.tickebook.theatreservice.service.TheatreService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TheatreServiceImpl implements TheatreService {
    private final TheatreRepository repo;
    private final TheatreOwnerProfileRepository ownerRepo;

    @Override
    public Theatre create(Theatre theatre) {
        theatre.setCreatedAt(LocalDateTime.now());
        theatre.setUpdatedAt(LocalDateTime.now());
        theatre.setIsActive(true);
        return repo.save(theatre);
    }

    @Override
    public List<Theatre> getByOwner(UUID ownerId) {
        return repo.findByOwnerProfileId(ownerId);
    }

    @Override
    public Theatre get(UUID id) {
        return repo.findById(id).orElseThrow(()-> new TheatreNotFoundException("Theatre not found with id "+id));
    }

    @Override
    public void deactivate(UUID id) {
        Theatre t = get(id);
        t.setIsActive(false);
        repo.save(t);
    }
}
