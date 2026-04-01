package org.team11.tickebook.consumerservice.service;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.consumerservice.client.PaymentClient;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.PaymentRequest;
import org.team11.tickebook.consumerservice.dto.PaymentResponse;
import org.team11.tickebook.consumerservice.dto.ShowSeat;
import org.team11.tickebook.consumerservice.model.*;
import org.team11.tickebook.consumerservice.repository.BookingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingPaymentServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private TicketService ticketService;

    @Mock
    private ShowClient showClient;

    @InjectMocks
    private BookingPaymentService service;

    // ================= SUCCESS FLOW =================

    @Test
    void payForBooking_shouldProcessPayment_confirmSeats_andGenerateTicket() {

        UUID bookingId = UUID.randomUUID();

        BookingSeat bookingSeat = new BookingSeat();
        bookingSeat.setSeatId(1L);
        bookingSeat.setShowId(UUID.randomUUID());

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setTotalAmount(BigDecimal.valueOf(200));
        booking.setSeats(List.of(bookingSeat));

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        when(paymentClient.processPayment(any(PaymentRequest.class)))
                .thenReturn(new PaymentResponse(true, "SUCCESS"));

        Ticket ticket = new Ticket();
        when(ticketService.generateTicket(any()))
                .thenReturn(ticket);

        Ticket result = service.payForBooking(bookingId);

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, booking.getBookingStatus());
        assertEquals(PaymentStatus.PAID, booking.getPaymentStatus());

        verify(paymentClient).processPayment(any());
        verify(showClient).confirmSeat(any(ShowSeat.class));
        verify(ticketService).generateTicket(any());
        verify(bookingRepository, atLeastOnce()).save(booking);
    }

    // ================= BOOKING NOT FOUND =================

    @Test
    void payForBooking_shouldThrowException_whenBookingNotFound() {

        UUID bookingId = UUID.randomUUID();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.payForBooking(bookingId));
    }

    // ================= INVALID STATUS =================

    @Test
    void payForBooking_shouldThrowException_whenNotPending() {

        UUID bookingId = UUID.randomUUID();

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class,
                () -> service.payForBooking(bookingId));
    }

    // ================= PAYMENT FAILURE =================

    @Test
    void payForBooking_shouldMarkFailed_andThrow_whenPaymentFails() {

        UUID bookingId = UUID.randomUUID();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setTotalAmount(BigDecimal.valueOf(200));

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        when(paymentClient.processPayment(any()))
                .thenReturn(new PaymentResponse(false, "FAILED"));

        assertThrows(RuntimeException.class,
                () -> service.payForBooking(bookingId));

        assertEquals(PaymentStatus.FAILED, booking.getPaymentStatus());
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());

        verify(bookingRepository).save(booking);
    }

    // ================= SEAT CONFIRM FAILURE =================

    @Test
    void payForBooking_shouldThrowException_whenSeatConfirmationFails() {

        UUID bookingId = UUID.randomUUID();

        BookingSeat bookingSeat = new BookingSeat();
        bookingSeat.setSeatId(1L);
        bookingSeat.setShowId(UUID.randomUUID());

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setTotalAmount(BigDecimal.valueOf(200));
        booking.setSeats(List.of(bookingSeat));

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        when(paymentClient.processPayment(any()))
                .thenReturn(new PaymentResponse(true, "SUCCESS"));

        doThrow(mock(FeignException.class))
                .when(showClient).confirmSeat(any());

        assertThrows(RuntimeException.class,
                () -> service.payForBooking(bookingId));
    }
}