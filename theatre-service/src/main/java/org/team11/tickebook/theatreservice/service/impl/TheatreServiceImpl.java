package org.team11.tickebook.theatreservice.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.exception.TheatreNotFoundException;
import org.team11.tickebook.theatreservice.model.Theatre;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.repository.TheatreRepository;
import org.team11.tickebook.theatreservice.security.CustomUserDetails;
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
    public Theatre create(Theatre theatre, Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();

        UUID userId = UUID.fromString(claims.get("userId", String.class));

        // Resolve owner profile from JWT
        TheatreOwnerProfile ownerProfile =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException(
                                        "Owner profile not found for user " + userId));

        // Inject internally (client cannot spoof)

        theatre.setOwnerProfile(ownerProfile);

        theatre.setCreatedAt(LocalDateTime.now());
        theatre.setUpdatedAt(LocalDateTime.now());
        theatre.setIsActive(true);

        return repo.save(theatre);
    }

    @Override
    public List<Theatre> getMyTheatres(Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();

        UUID userId = UUID.fromString(claims.get("userId", String.class));


        TheatreOwnerProfile ownerProfile =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException(
                                        "Owner profile not found"));

        return repo.findByOwnerProfile(ownerProfile);
    }

    @Override
    public Theatre get(UUID id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new TheatreNotFoundException(
                                "Theatre not found with id " + id));
    }

    @Override
    public void deactivate(UUID id, Authentication authentication) {
        Claims claims = (Claims) authentication.getPrincipal();

        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile ownerProfile =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException(
                                        "Owner profile not found"));

        Theatre theatre = get(id);

        // Ownership check
        if (!theatre.getOwnerProfile().getId().equals(ownerProfile.getId())) {
            throw new IllegalArgumentException(
                    "You do not own this theatre");
        }

        theatre.setIsActive(false);
        theatre.setUpdatedAt(LocalDateTime.now());
        repo.save(theatre);
    }
}
