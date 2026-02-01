package org.team11.tickebook.theatreservice.controller;

import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public Theatre create(@RequestBody Theatre theatre) {
        return service.create(theatre);
    }

    @GetMapping("/{id}")
    public Theatre get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Theatre> byOwner(@PathVariable UUID ownerId) {
        return service.getByOwner(ownerId);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
