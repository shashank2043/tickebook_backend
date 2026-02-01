package org.team11.tickebook.theatreservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.theatreservice.model.Seat;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService service;

    @PostMapping
    public Seat create(@RequestBody Seat s) {
        return service.create(s);
    }

    @GetMapping("/screen/{screenId}")
    public List<Seat> getByScreen(@PathVariable Long screenId) {
        return service.getByScreen(screenId);
    }
}

