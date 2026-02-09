package org.team11.tickebook.theatreservice.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.dto.request.CreateSeatRequest;
import org.team11.tickebook.theatreservice.dto.response.SeatDto;
import org.team11.tickebook.theatreservice.exception.OwnerProfileNotFoundException;
import org.team11.tickebook.theatreservice.model.Screen;
import org.team11.tickebook.theatreservice.model.Seat;
import org.team11.tickebook.theatreservice.model.TheatreOwnerProfile;
import org.team11.tickebook.theatreservice.repository.ScreenRepository;
import org.team11.tickebook.theatreservice.repository.SeatRepository;
import org.team11.tickebook.theatreservice.repository.TheatreOwnerProfileRepository;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository repo;
    private final ScreenRepository screenRepo;
    private final TheatreOwnerProfileRepository ownerRepo;

    @Override
    public Seat create(CreateSeatRequest request, Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile owner =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() -> new OwnerProfileNotFoundException("Owner not found"));

        Screen screen = screenRepo.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        // Ownership validation
        if (!screen.getTheatre().getOwnerProfile().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        Seat seat = new Seat();
        seat.setRowLabel(request.getRowLabel());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        seat.setPositionX(request.getPositionX());
        seat.setPositionY(request.getPositionY());
        seat.setActive(true);
        seat.setScreen(screen);

        return repo.save(seat);
    }

    @Override
    public List<Seat> getByScreen(Long screenId, Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile owner =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() -> new OwnerProfileNotFoundException("Owner not found"));

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        if (!screen.getTheatre().getOwnerProfile().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        return repo.findByScreen(screen);
    }

    @Override
    public void deactivate(Long seatId, Authentication authentication) {

        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile owner =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() -> new OwnerProfileNotFoundException("Owner not found"));

        Seat seat = repo.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.getScreen().getTheatre().getOwnerProfile().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        seat.setActive(false);
        repo.save(seat);
    }

    @Override
    public List<SeatDto> getByScreenAsDto(Long screenId, Authentication authentication) {
        Claims claims = (Claims) authentication.getPrincipal();
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        TheatreOwnerProfile owner =
                ownerRepo.findByUserId(userId)
                        .orElseThrow(() -> new OwnerProfileNotFoundException("Owner not found"));

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        if (!screen.getTheatre().getOwnerProfile().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        return repo.findByScreen(screen)
                .stream()
                .map(seat ->
                        new SeatDto(seat.getId(),
                                seat.getRowLabel(),
                                seat.getSeatNumber(),
                                seat.getSeatType(),
                                seat.getPositionX(),
                                seat.getPositionY(),
                                seat.isActive())
                ).toList();
    }
}

