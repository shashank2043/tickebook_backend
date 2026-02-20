package org.team11.tickebook.consumerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.consumerservice.model.BookingSeat;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.service.BookingSeatService;
//import org.team11.tickebook.consumerservice.service.InternalBookingService;
import org.team11.tickebook.consumerservice.service.TicketService;

import java.util.UUID;

@RequestMapping("/internal/booking")
@RequiredArgsConstructor
@RestController
public class InternalController {
    private final BookingSeatService bookingSeatService;
    private final TicketService ticketService;
//    private final InternalBookingService internalBookingService;


    //    @PostMapping
//    public ResponseEntity<BookingSeat> create(@RequestBody BookingSeat bookingSeat) {
//        return ResponseEntity.ok(bookingSeatService.createBookingSeat(bookingSeat));
//    }
//    @PostMapping("/ticket")
//    public ResponseEntity<Ticket> generateTicket(@RequestBody Ticket ticket) {
//        return ResponseEntity.ok(ticketService.generateTicket(ticket));
//    }
//    @PutMapping("/{id}/confirm")
//    public ResponseEntity<Ticket> confirmBooking(@PathVariable UUID id) {
//        internalBookingService.confirmBooking(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{id}/fail")
//    public ResponseEntity<Void> failBooking(@PathVariable UUID id) {
//        internalBookingService.failBooking(id);
//        return ResponseEntity.ok().build();
//    }
}
