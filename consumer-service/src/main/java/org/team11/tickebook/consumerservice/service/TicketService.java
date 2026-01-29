package org.team11.tickebook.consumerservice.service;

import org.team11.tickebook.consumerservice.model.Ticket;

import java.util.UUID;

public interface TicketService {

    Ticket generateTicket(Ticket ticket);

    Ticket getTicketById(UUID ticketId);

    Ticket getTicketByBookingId(UUID bookingId);

    void deleteTicket(UUID ticketId);
}
