package org.team11.tickebook.consumerservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.consumerservice.model.BookingSeat;
import org.team11.tickebook.consumerservice.service.BookingSeatService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingSeatControllerTest {

    @Mock
    private BookingSeatService bookingSeatService;

    @InjectMocks
    private BookingSeatController controller;

    // ================= GET BY ID =================

    @Test
    void getById_shouldReturnSeat_whenExists() {

        UUID id = UUID.randomUUID();
        BookingSeat seat = new BookingSeat();

        when(bookingSeatService.getBookingSeatById(id)).thenReturn(seat);

        ResponseEntity<BookingSeat> response = controller.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seat, response.getBody());

        verify(bookingSeatService).getBookingSeatById(id);
    }

    @Test
    void getById_shouldPropagateException_whenServiceFails() {

        UUID id = UUID.randomUUID();

        when(bookingSeatService.getBookingSeatById(id))
                .thenThrow(new RuntimeException("Not found"));

        assertThrows(RuntimeException.class,
                () -> controller.getById(id));

        verify(bookingSeatService).getBookingSeatById(id);
    }

    // ================= GET BY BOOKING =================

    @Test
    void getByBooking_shouldReturnSeatList_whenExists() {

        UUID bookingId = UUID.randomUUID();
        List<BookingSeat> seats = List.of(new BookingSeat(), new BookingSeat());

        when(bookingSeatService.getSeatsByBookingId(bookingId)).thenReturn(seats);

        ResponseEntity<List<BookingSeat>> response =
                controller.getByBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seats, response.getBody());

        verify(bookingSeatService).getSeatsByBookingId(bookingId);
    }

    @Test
    void getByBooking_shouldReturnEmptyList_whenNoSeats() {

        UUID bookingId = UUID.randomUUID();

        when(bookingSeatService.getSeatsByBookingId(bookingId))
                .thenReturn(List.of());

        ResponseEntity<List<BookingSeat>> response =
                controller.getByBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // ================= GET BY SHOW =================

    @Test
    void getByShow_shouldReturnSeatList_whenExists() {

        UUID showId = UUID.randomUUID();
        List<BookingSeat> seats = List.of(new BookingSeat());

        when(bookingSeatService.getSeatsByShowId(showId)).thenReturn(seats);

        ResponseEntity<List<BookingSeat>> response =
                controller.getByShow(showId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seats, response.getBody());

        verify(bookingSeatService).getSeatsByShowId(showId);
    }

    // ================= UPDATE STATUS =================

    @Test
    void updateStatus_shouldUpdateAndReturnSeat_whenValid() {

        UUID id = UUID.randomUUID();
        String status = "BOOKED";

        BookingSeat updatedSeat = new BookingSeat();

        when(bookingSeatService.updateSeatStatus(id, status))
                .thenReturn(updatedSeat);

        ResponseEntity<BookingSeat> response =
                controller.updateStatus(id, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSeat, response.getBody());

        verify(bookingSeatService).updateSeatStatus(id, status);
    }

    @Test
    void updateStatus_shouldPropagateException_whenServiceFails() {

        UUID id = UUID.randomUUID();
        String status = "BOOKED";

        when(bookingSeatService.updateSeatStatus(id, status))
                .thenThrow(new RuntimeException("Update failed"));

        assertThrows(RuntimeException.class,
                () -> controller.updateStatus(id, status));

        verify(bookingSeatService).updateSeatStatus(id, status);
    }

    // ================= DELETE =================

    @Test
    void delete_shouldCallService_andReturnNoContent() {

        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(bookingSeatService).deleteBookingSeat(id);
    }

    @Test
    void delete_shouldPropagateException_whenServiceFails() {

        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException("Delete failed"))
                .when(bookingSeatService).deleteBookingSeat(id);

        assertThrows(RuntimeException.class,
                () -> controller.delete(id));

        verify(bookingSeatService).deleteBookingSeat(id);
    }
}