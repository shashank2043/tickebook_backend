package org.team11.tickebook.consumerservice.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.repository.TicketRepository;
import org.team11.tickebook.consumerservice.service.TicketService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    @Autowired
    private  TicketRepository ticketRepository;

    @Override
    public Ticket generateTicket(Ticket ticket) {
        ticket.setId(UUID.randomUUID());
        ticket.setIssuedAt(LocalDateTime.now());
        ticket.setQrCode("QR-" + ticket.getId()); // later replace with real QR logic
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
    }

    @Override
    public Ticket getTicketByBookingId(UUID bookingId) {
        return ticketRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new RuntimeException("Ticket not found for booking: " + bookingId));
    }

    @Override
    public void deleteTicket(UUID ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}
