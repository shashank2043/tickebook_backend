package org.team11.tickebook.theatreservice.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.theatreservice.dto.request.CreateScreenRequest;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.exception.TheatreNotFoundException;
import org.team11.tickebook.theatreservice.model.Screen;
import org.team11.tickebook.theatreservice.model.Theatre;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.repository.ScreenRepository;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.repository.TheatreRepository;
import org.team11.tickebook.theatreservice.service.ScreenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
//@Service
//@RequiredArgsConstructor
//public class ScreenServiceImpl implements ScreenService {
//    private final ScreenRepository repo;
//    @Override
//    public Screen create(Screen screen, Authentication authentication) {
//        screen.setCreatedAt(LocalDateTime.now());
//        screen.setUpdatedAt(LocalDateTime.now());
//        screen.setIsActive(true);
//        return repo.save(screen);
//    }
//
//    @Override
//    public List<Screen> getByTheatre(UUID theatreId, Authentication authentication) {
//        return repo.findByTheatreId(theatreId);
//    }
//}
@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository repo;
    private final TheatreRepository theatreRepo;
    private final TheatreOwnerProfileRepository ownerRepo;

    @Override
    @Transactional
    public Screen create(CreateScreenRequest screenRequest,
                         Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile owner =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException("Owner not found"));

        Theatre theatre = theatreRepo.findById(screenRequest.getTheatreId())
                .orElseThrow(() ->
                        new TheatreNotFoundException("Theatre not found"));

        if (theatre.getOwnerProfile() == null ||
                !theatre.getOwnerProfile().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("You do not own this theatre");
        }

        Screen screen = new Screen();
        screen.setId(null);
        screen.setName(screenRequest.getName());
        screen.setScreenNumber(screenRequest.getScreenNumber());
        screen.setTotalSeats(screenRequest.getTotalSeats());
        screen.setTheatre(theatre);
        screen.setIsActive(true);
        screen.setCreatedAt(LocalDateTime.now());
        screen.setUpdatedAt(LocalDateTime.now());

        return repo.save(screen);
    }

    @Override
    public List<Screen> getByTheatre(UUID theatreId, Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile owner =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() ->
                                new OwnerProfileNotFoundException("Owner not found"));

        Theatre theatre = theatreRepo.findById(theatreId)
                .orElseThrow(() -> new TheatreNotFoundException("Not found"));

        if (!theatre.getOwnerProfile().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        return repo.findByTheatre(theatre);
    }
}
