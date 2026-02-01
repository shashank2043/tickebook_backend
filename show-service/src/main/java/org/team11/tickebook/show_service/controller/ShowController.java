package org.team11.tickebook.show_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.show_service.dto.CreateShowRequestDto;
import org.team11.tickebook.show_service.dto.UpdateShowRequestDto;
import org.team11.tickebook.show_service.model.Show;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.service.ShowService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    // CREATE SHOW
    @PostMapping
    public ResponseEntity<Show> createShow(
            @RequestBody CreateShowRequestDto req) {

        Show show = showService.createShow(
                req.getMovieId(),
                req.getScreenId(),
                req.getStartTime(),
                req.getEndTime(),
                req.getPriceMap()
        );

        return ResponseEntity.ok(show);
    }

    // GET SEATS OF SHOW
    @GetMapping("/{showId}/seats")
    public ResponseEntity<List<ShowSeat>> getSeats(
            @PathVariable UUID showId) {

        return ResponseEntity.ok(showService.getSeats(showId));
    }

    // UPDATE SHOW
    @PutMapping("/{showId}")
    public ResponseEntity<Show> updateShow(
            @PathVariable UUID showId,
            @RequestBody UpdateShowRequestDto req) {

        Show updated = showService.updateShow(
                showId,
                req.getStartTime(),
                req.getEndTime()
        );

        return ResponseEntity.ok(updated);
    }

    // DELETE SHOW (SOFT DELETE)
    @DeleteMapping("/{showId}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable UUID showId) {

        showService.deleteShow(showId);
        return ResponseEntity.noContent().build();
    }
}
