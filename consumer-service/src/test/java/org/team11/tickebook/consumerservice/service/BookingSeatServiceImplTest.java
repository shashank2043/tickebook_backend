package org.team11.tickebook.consumerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.consumerservice.exception.BookingSeatException;
import org.team11.tickebook.consumerservice.model.BookingSeat;
import org.team11.tickebook.consumerservice.model.BookingSeatStatus;
import org.team11.tickebook.consumerservice.repository.BookingSeatRepository;
import org.team11.tickebook.consumerservice.service.impl.BookingSeatServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingSeatServiceImplTest {

    @Mock
    private BookingSeatRepository bookingSeatRepository;

    @InjectMocks
    private BookingSeatServiceImpl service;

    // ================= CREATE =================

    @Test
    void createBookingSeat_shouldSetStatusBooked_andSave() {

        BookingSeat seat = new BookingSeat();

        when(bookingSeatRepository.save(any(BookingSeat.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingSeat result = service.createBookingSeat(seat);

        assertEquals(BookingSeatStatus.BOOKED, result.getStatus());
        verify(bookingSeatRepository).save(seat);
    }

    // ================= GET BY ID =================

    @Test
    void getBookingSeatById_shouldReturnSeat_whenExists() {

        UUID id = UUID.randomUUID();
        BookingSeat seat = new BookingSeat();

        when(bookingSeatRepository.findById(id))
                .thenReturn(Optional.of(seat));

        BookingSeat result = service.getBookingSeatById(id);

        assertEquals(seat, result);
        verify(bookingSeatRepository).findById(id);
    }

    @Test
    void getBookingSeatById_shouldThrowException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(bookingSeatRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(BookingSeatException.class,
                () -> service.getBookingSeatById(id));

        verify(bookingSeatRepository).findById(id);
    }

    // ================= GET BY BOOKING =================

    @Test
    void getSeatsByBookingId_shouldReturnList() {

        UUID bookingId = UUID.randomUUID();
        List<BookingSeat> seats = List.of(new BookingSeat());

        when(bookingSeatRepository.findByBooking_Id(bookingId))
                .thenReturn(seats);

        List<BookingSeat> result = service.getSeatsByBookingId(bookingId);

        assertEquals(seats, result);
        verify(bookingSeatRepository).findByBooking_Id(bookingId);
    }

    @Test
    void getSeatsByBookingId_shouldReturnEmptyList_whenNone() {

        UUID bookingId = UUID.randomUUID();

        when(bookingSeatRepository.findByBooking_Id(bookingId))
                .thenReturn(List.of());

        List<BookingSeat> result = service.getSeatsByBookingId(bookingId);

        assertTrue(result.isEmpty());
    }

    // ================= GET BY SHOW =================

    @Test
    void getSeatsByShowId_shouldReturnList() {

        UUID showId = UUID.randomUUID();
        List<BookingSeat> seats = List.of(new BookingSeat());

        when(bookingSeatRepository.findByShowId(showId))
                .thenReturn(seats);

        List<BookingSeat> result = service.getSeatsByShowId(showId);

        assertEquals(seats, result);
        verify(bookingSeatRepository).findByShowId(showId);
    }

    // ================= UPDATE STATUS =================

    @Test
    void updateSeatStatus_shouldUpdateStatus_andSave() {

        UUID id = UUID.randomUUID();
        BookingSeat seat = new BookingSeat();

        when(bookingSeatRepository.findById(id))
                .thenReturn(Optional.of(seat));

        when(bookingSeatRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingSeat result = service.updateSeatStatus(id, "BOOKED");

        assertEquals(BookingSeatStatus.BOOKED, result.getStatus());
        verify(bookingSeatRepository).save(seat);
    }

    @Test
    void updateSeatStatus_shouldThrowException_whenSeatNotFound() {

        UUID id = UUID.randomUUID();

        when(bookingSeatRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(BookingSeatException.class,
                () -> service.updateSeatStatus(id, "BOOKED"));
    }

    @Test
    void updateSeatStatus_shouldThrowException_whenInvalidStatus() {

        UUID id = UUID.randomUUID();
        BookingSeat seat = new BookingSeat();

        when(bookingSeatRepository.findById(id))
                .thenReturn(Optional.of(seat));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateSeatStatus(id, "INVALID_STATUS"));
    }

    // ================= DELETE =================

    @Test
    void deleteBookingSeat_shouldCallRepository() {

        UUID id = UUID.randomUUID();

        service.deleteBookingSeat(id);

        verify(bookingSeatRepository).deleteById(id);
    }

    @Test
    void deleteBookingSeat_shouldPropagateException_whenFails() {

        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException("Delete failed"))
                .when(bookingSeatRepository).deleteById(id);

        assertThrows(RuntimeException.class,
                () -> service.deleteBookingSeat(id));

        verify(bookingSeatRepository).deleteById(id);
    }
}