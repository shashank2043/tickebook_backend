package org.team11.tickebook.consumerservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.service.TicketService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController controller;

    // ================= GET TICKET BY ID =================

    @Test
    void getTicket_shouldReturnTicket_whenExists() {

        UUID ticketId = UUID.randomUUID();
        Ticket ticket = new Ticket();

        when(ticketService.getTicketById(ticketId)).thenReturn(ticket);

        ResponseEntity<Ticket> response = controller.getTicket(ticketId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());

        verify(ticketService).getTicketById(ticketId);
    }

    @Test
    void getTicket_shouldPropagateException_whenNotFound() {

        UUID ticketId = UUID.randomUUID();

        when(ticketService.getTicketById(ticketId))
                .thenThrow(new RuntimeException("Ticket not found"));

        assertThrows(RuntimeException.class,
                () -> controller.getTicket(ticketId));

        verify(ticketService).getTicketById(ticketId);
    }

    // ================= GET BY BOOKING =================

    @Test
    void getTicketByBooking_shouldReturnTicket_whenExists() {

        UUID bookingId = UUID.randomUUID();
        Ticket ticket = new Ticket();

        when(ticketService.getTicketByBookingId(bookingId))
                .thenReturn(ticket);

        ResponseEntity<Ticket> response =
                controller.getTicketByBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());

        verify(ticketService).getTicketByBookingId(bookingId);
    }

    @Test
    void getTicketByBooking_shouldPropagateException_whenNotFound() {

        UUID bookingId = UUID.randomUUID();

        when(ticketService.getTicketByBookingId(bookingId))
                .thenThrow(new RuntimeException("Not found"));

        assertThrows(RuntimeException.class,
                () -> controller.getTicketByBooking(bookingId));

        verify(ticketService).getTicketByBookingId(bookingId);
    }

    // ================= DELETE =================

    @Test
    void deleteTicket_shouldCallService_andReturnNoContent() {

        UUID ticketId = UUID.randomUUID();

        ResponseEntity<Void> response =
                controller.deleteTicket(ticketId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(ticketService).deleteTicket(ticketId);
    }

    @Test
    void deleteTicket_shouldPropagateException_whenServiceFails() {

        UUID ticketId = UUID.randomUUID();

        doThrow(new RuntimeException("Delete failed"))
                .when(ticketService).deleteTicket(ticketId);

        assertThrows(RuntimeException.class,
                () -> controller.deleteTicket(ticketId));

        verify(ticketService).deleteTicket(ticketId);
    }
}