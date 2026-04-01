package org.team11.tickebook.consumerservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.consumerservice.dto.request.BookingRequestDto;
import org.team11.tickebook.consumerservice.dto.response.BookingResponseDto;
import org.team11.tickebook.consumerservice.model.Ticket;
import org.team11.tickebook.consumerservice.service.BookingPaymentService;
import org.team11.tickebook.consumerservice.service.BookingService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private BookingPaymentService bookingPaymentService;

    @Mock
    private HttpServletRequest servletRequest;

    @InjectMocks
    private BookingController controller;

    // ================= CREATE BOOKING =================

    @Test
    void createBooking_shouldSetUserId_andReturnCreated_whenValidRequest() {

        UUID userId = UUID.randomUUID();
        BookingRequestDto request = new BookingRequestDto();

        BookingResponseDto responseDto = new BookingResponseDto();

        when(servletRequest.getHeader("X-User-Id"))
                .thenReturn(userId.toString());

        when(bookingService.createBooking(any()))
                .thenReturn(responseDto);

        ResponseEntity<BookingResponseDto> response =
                controller.createBooking(request, servletRequest);

        // ✅ Status
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ✅ Body
        assertEquals(responseDto, response.getBody());

        // ✅ Verify userId set
        assertEquals(userId, request.getUserId());

        verify(bookingService).createBooking(request);
    }

    @Test
    void createBooking_shouldThrowException_whenHeaderMissing() {

        BookingRequestDto request = new BookingRequestDto();

        when(servletRequest.getHeader("X-User-Id"))
                .thenReturn(null);

        assertThrows(Exception.class,
                () -> controller.createBooking(request, servletRequest));
    }

    // ================= GET BOOKING =================

    @Test
    void getBookingById_shouldReturnBooking_whenExists() {

        UUID bookingId = UUID.randomUUID();
        BookingResponseDto responseDto = new BookingResponseDto();

        when(bookingService.getBookingById(bookingId))
                .thenReturn(responseDto);

        ResponseEntity<BookingResponseDto> response =
                controller.getBookingById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(bookingService).getBookingById(bookingId);
    }

    // ================= PAYMENT =================

    @Test
    void pay_shouldReturnTicket_whenPaymentSuccessful() {

        UUID bookingId = UUID.randomUUID();
        Ticket ticket = new Ticket();

        when(bookingPaymentService.payForBooking(bookingId))
                .thenReturn(ticket);

        ResponseEntity<Ticket> response =
                controller.pay(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());

        verify(bookingPaymentService).payForBooking(bookingId);
    }

    @Test
    void pay_shouldPropagateException_whenPaymentFails() {

        UUID bookingId = UUID.randomUUID();

        when(bookingPaymentService.payForBooking(bookingId))
                .thenThrow(new RuntimeException("Payment failed"));

        assertThrows(RuntimeException.class,
                () -> controller.pay(bookingId));

        verify(bookingPaymentService).payForBooking(bookingId);
    }
}