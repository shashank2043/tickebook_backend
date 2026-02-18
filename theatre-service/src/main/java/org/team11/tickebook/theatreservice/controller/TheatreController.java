package org.team11.tickebook.theatreservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.model.Theatre;
import org.team11.tickebook.theatreservice.service.TheatreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService service;

    // Create theatre for logged-in owner only
    @PostMapping
    public Theatre create(
            @RequestBody @Valid Theatre theatre,
            Authentication authentication
    ) {
        return service.create(theatre, authentication);
    }

    // Get single theatre (still allowed by id)
    @GetMapping("/{id}")
    public Theatre get(@PathVariable UUID id) {
        return service.get(id);
    }

    // Get MY theatres only
    @GetMapping("/me")
    public List<Theatre> myTheatres(Authentication authentication) {
        return service.getMyTheatres(authentication);
    }

    // Deactivate only if owned by logged-in user
    @DeleteMapping("/{id}")
    public void deactivate(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        service.deactivate(id, authentication);
    }
}
