package org.team11.tickebook.consumerservice.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.consumerservice.model.BookingSeat;
import org.team11.tickebook.consumerservice.service.BookingSeatService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking-seats")
@RequiredArgsConstructor
public class BookingSeatController {

    private final BookingSeatService bookingSeatService;

    @PostMapping
    public ResponseEntity<BookingSeat> create(@RequestBody BookingSeat bookingSeat) {
        return ResponseEntity.ok(bookingSeatService.createBookingSeat(bookingSeat));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingSeat> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingSeatService.getBookingSeatById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookingSeat>> getByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(bookingSeatService.getSeatsByBookingId(bookingId));
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<BookingSeat>> getByShow(@PathVariable UUID showId) {
        return ResponseEntity.ok(bookingSeatService.getSeatsByShowId(showId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingSeat> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(bookingSeatService.updateSeatStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookingSeatService.deleteBookingSeat(id);
        return ResponseEntity.noContent().build();
    }
}
