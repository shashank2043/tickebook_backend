package org.team11.tickebook.consumerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.ShowSeat;
import org.team11.tickebook.consumerservice.dto.ShowSeatStatus;
import org.team11.tickebook.consumerservice.dto.request.BookingRequestDto;
import org.team11.tickebook.consumerservice.dto.response.BookingResponseDto;
import org.team11.tickebook.consumerservice.exception.BookingException;
import org.team11.tickebook.consumerservice.model.Booking;
import org.team11.tickebook.consumerservice.repository.BookingRepository;
import org.team11.tickebook.consumerservice.service.impl.BookingServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowClient showClient;

    @InjectMocks
    private BookingServiceImpl service;

    // ================= CREATE BOOKING =================

    @Test
    void createBooking_shouldCreateBooking_whenValidRequest() {

        UUID userId = UUID.randomUUID();
        UUID showId = UUID.randomUUID();
        Long seatId = 1L;

        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(userId);
        request.setShowId(showId);
        request.setSeatId(List.of(seatId));

        ShowSeat seat = new ShowSeat();
        seat.setSeatId(seatId);
        seat.setShowId(showId);
        seat.setStatus(ShowSeatStatus.AVAILABLE);
        seat.setPrice(BigDecimal.valueOf(200));

        when(showClient.getSeats(showId)).thenReturn(List.of(seat));
        when(showClient.lockSeat(any())).thenReturn(seat);

        when(bookingRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponseDto result = service.createBooking(request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(showId, result.getShow());

        verify(showClient).getSeats(showId);
        verify(showClient).lockSeat(any());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenNoSeatsSelected() {

        BookingRequestDto request = new BookingRequestDto();
        request.setSeatId(List.of());

        assertThrows(RuntimeException.class,
                () -> service.createBooking(request));
    }

    @Test
    void createBooking_shouldThrowException_whenSeatNotFound() {

        UUID showId = UUID.randomUUID();

        BookingRequestDto request = new BookingRequestDto();
        request.setShowId(showId);
        request.setSeatId(List.of(1L));

        when(showClient.getSeats(showId)).thenReturn(List.of()); // no seats

        assertThrows(RuntimeException.class,
                () -> service.createBooking(request));
    }

    @Test
    void createBooking_shouldThrowException_whenSeatNotAvailable() {

        UUID showId = UUID.randomUUID();

        BookingRequestDto request = new BookingRequestDto();
        request.setShowId(showId);
        request.setSeatId(List.of(1L));

        ShowSeat seat = new ShowSeat();
        seat.setSeatId(1L);
        seat.setStatus(ShowSeatStatus.BOOKED); // not available

        when(showClient.getSeats(showId)).thenReturn(List.of(seat));

        assertThrows(RuntimeException.class,
                () -> service.createBooking(request));
    }

    @Test
    void createBooking_shouldThrowException_whenLockFails() {

        UUID showId = UUID.randomUUID();

        BookingRequestDto request = new BookingRequestDto();
        request.setShowId(showId);
        request.setSeatId(List.of(1L));

        ShowSeat seat = new ShowSeat();
        seat.setSeatId(1L);
        seat.setStatus(ShowSeatStatus.AVAILABLE);

        when(showClient.getSeats(showId)).thenReturn(List.of(seat));
        when(showClient.lockSeat(any())).thenReturn(null); // lock fail

        assertThrows(BookingException.class,
                () -> service.createBooking(request));
    }

    // ================= GET BOOKING =================

    @Test
    void getBookingById_shouldReturnBooking_whenExists() {

        UUID bookingId = UUID.randomUUID();
        Booking booking = new Booking();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));

        BookingResponseDto result = service.getBookingById(bookingId);

        assertNotNull(result);
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void getBookingById_shouldThrowException_whenNotFound() {

        UUID bookingId = UUID.randomUUID();

        when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.empty());

        assertThrows(BookingException.class,
                () -> service.getBookingById(bookingId));
    }
}