package org.team11.tickebook.theatreservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.dto.request.CreateSeatRequest;
import org.team11.tickebook.theatreservice.model.Seat;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService service;

    @PostMapping
    public Seat create(
            @RequestBody @Valid CreateSeatRequest request,
            Authentication authentication
    ) {
        return service.create(request, authentication);
    }

    @GetMapping("/screen/{screenId}")
    public List<Seat> getByScreen(
            @PathVariable Long screenId,
            Authentication authentication
    ) {
        return service.getByScreen(screenId, authentication);
    }

    @DeleteMapping("/{seatId}")
    public void deactivate(
            @PathVariable Long seatId,
            Authentication authentication
    ) {
        service.deactivate(seatId, authentication);
    }
}
