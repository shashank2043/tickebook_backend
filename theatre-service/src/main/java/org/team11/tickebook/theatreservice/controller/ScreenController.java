package org.team11.tickebook.theatreservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.dto.request.CreateScreenRequest;
import org.team11.tickebook.theatreservice.model.Screen;
import org.team11.tickebook.theatreservice.service.ScreenService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService service;

    @PostMapping
    public Screen create(@RequestBody CreateScreenRequest screenRequest, Authentication authentication) {
        return service.create(screenRequest,authentication);
    }

    @GetMapping("/theatre/{theatreId}")
    public List<Screen> getByTheatre(@PathVariable UUID theatreId,Authentication authentication) {
        return service.getByTheatre(theatreId,authentication);
    }
}
