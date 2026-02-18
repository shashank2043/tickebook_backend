package org.team11.tickebook.consumerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.team11.tickebook.consumerservice.model.BookingSeat;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.service.BookingSeatService;
import org.team11.tickebook.consumerservice.service.TicketService;

@RequestMapping("/internal/booking")
@RequiredArgsConstructor
public class InternalController {
    private final BookingSeatService bookingSeatService;
    private final TicketService ticketService;

    //    @PostMapping
//    public ResponseEntity<BookingSeat> create(@RequestBody BookingSeat bookingSeat) {
//        return ResponseEntity.ok(bookingSeatService.createBookingSeat(bookingSeat));
//    }
    @PostMapping("/ticket")
    public ResponseEntity<Ticket> generateTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.generateTicket(ticket));
    }
}
