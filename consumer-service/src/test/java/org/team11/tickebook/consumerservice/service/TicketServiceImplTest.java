package org.team11.tickebook.consumerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.consumerservice.model.Booking;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.repository.TicketRepository;
import org.team11.tickebook.consumerservice.service.impl.TicketServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl service;

    // ================= GENERATE TICKET =================

    @Test
    void generateTicket_shouldSetFields_andSaveTicket() {

        UUID bookingId = UUID.randomUUID();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingNumber("BOOK123");

        Ticket ticket = new Ticket();
        ticket.setBooking(booking);

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = service.generateTicket(ticket);

        // ✅ Verify QR Code
        assertEquals("QR-BOOK123", result.getQrCode());

        // ✅ Verify issuedAt set
        assertNotNull(result.getIssuedAt());

        // (Optional) time sanity check
        assertTrue(result.getIssuedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        verify(ticketRepository).save(ticket);
    }

    // ================= GET BY ID =================

    @Test
    void getTicketById_shouldReturnTicket_whenExists() {

        UUID ticketId = UUID.randomUUID();
        Ticket ticket = new Ticket();

        when(ticketRepository.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        Ticket result = service.getTicketById(ticketId);

        assertEquals(ticket, result);

        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void getTicketById_shouldThrowException_whenNotFound() {

        UUID ticketId = UUID.randomUUID();

        when(ticketRepository.findById(ticketId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getTicketById(ticketId));

        verify(ticketRepository).findById(ticketId);
    }

    // ================= GET BY BOOKING ID =================

    @Test
    void getTicketByBookingId_shouldReturnTicket_whenExists() {

        UUID bookingId = UUID.randomUUID();
        Ticket ticket = new Ticket();

        when(ticketRepository.findByBooking_Id(bookingId))
                .thenReturn(Optional.of(ticket));

        Ticket result = service.getTicketByBookingId(bookingId);

        assertEquals(ticket, result);

        verify(ticketRepository).findByBooking_Id(bookingId);
    }

    @Test
    void getTicketByBookingId_shouldThrowException_whenNotFound() {

        UUID bookingId = UUID.randomUUID();

        when(ticketRepository.findByBooking_Id(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getTicketByBookingId(bookingId));

        verify(ticketRepository).findByBooking_Id(bookingId);
    }

    // ================= DELETE =================

    @Test
    void deleteTicket_shouldCallRepository() {

        UUID ticketId = UUID.randomUUID();

        service.deleteTicket(ticketId);

        verify(ticketRepository).deleteById(ticketId);
    }

    @Test
    void deleteTicket_shouldPropagateException_whenRepositoryFails() {

        UUID ticketId = UUID.randomUUID();

        doThrow(new RuntimeException("Delete failed"))
                .when(ticketRepository).deleteById(ticketId);

        assertThrows(RuntimeException.class,
                () -> service.deleteTicket(ticketId));

        verify(ticketRepository).deleteById(ticketId);
    }
}