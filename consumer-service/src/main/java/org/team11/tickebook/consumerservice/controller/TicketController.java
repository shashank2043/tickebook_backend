package org.team11.tickebook.consumerservice.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.service.TicketService;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> generateTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.generateTicket(ticket));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable UUID ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Ticket> getTicketByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(ticketService.getTicketByBookingId(bookingId));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}

