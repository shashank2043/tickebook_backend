package org.team11.tickebook.show_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.show_service.dto.CreateShowRequestDto;
import org.team11.tickebook.show_service.dto.UpdateShowRequestDto;
import org.team11.tickebook.show_service.model.Show;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.service.ShowService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowControllerTest {

    @Mock
    private ShowService showService;

    @InjectMocks
    private ShowController controller;

    // ================= CREATE =================

    @Test
    void createShow_shouldReturnShow_whenValidRequest() {

        CreateShowRequestDto req = new CreateShowRequestDto();
        req.setMovieId(UUID.randomUUID());
        req.setScreenId(1L);
        req.setStartTime(LocalDateTime.now());
        req.setEndTime(LocalDateTime.now().plusHours(2));
        req.setPriceMap(Map.of());

        Show show = new Show();

        when(showService.createShow(
                req.getMovieId(),
                req.getScreenId(),
                req.getStartTime(),
                req.getEndTime(),
                req.getPriceMap()
        )).thenReturn(show);

        ResponseEntity<Show> response = controller.createShow(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(show, response.getBody());

        verify(showService).createShow(
                req.getMovieId(),
                req.getScreenId(),
                req.getStartTime(),
                req.getEndTime(),
                req.getPriceMap()
        );
    }

    // ================= GET SEATS =================

    @Test
    void getSeats_shouldReturnSeatList() {

        UUID showId = UUID.randomUUID();
        List<ShowSeat> seats = List.of(new ShowSeat());

        when(showService.getSeats(showId)).thenReturn(seats);

        ResponseEntity<List<ShowSeat>> response =
                controller.getSeats(showId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seats, response.getBody());

        verify(showService).getSeats(showId);
    }

    // ================= GET ALL =================

    @Test
    void getAllShows_shouldReturnList() {

        List<Show> shows = List.of(new Show(), new Show());

        when(showService.getAllShows()).thenReturn(shows);

        ResponseEntity<List<Show>> response =
                controller.getAllShows();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shows, response.getBody());

        verify(showService).getAllShows();
    }

    // ================= GET ONE =================

    @Test
    void getShow_shouldReturnShow_whenExists() {

        UUID id = UUID.randomUUID();
        Show show = new Show();

        when(showService.getShow(id)).thenReturn(show);

        ResponseEntity<Show> response = controller.getShow(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(show, response.getBody());

        verify(showService).getShow(id);
    }

    @Test
    void getShow_shouldPropagateException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(showService.getShow(id))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.getShow(id));
    }

    // ================= UPDATE =================

    @Test
    void updateShow_shouldReturnUpdatedShow() {

        UUID showId = UUID.randomUUID();

        UpdateShowRequestDto req = new UpdateShowRequestDto();
        req.setStartTime(LocalDateTime.now());
        req.setEndTime(LocalDateTime.now().plusHours(2));

        Show updated = new Show();

        when(showService.updateShow(
                showId,
                req.getStartTime(),
                req.getEndTime()
        )).thenReturn(updated);

        ResponseEntity<Show> response =
                controller.updateShow(showId, req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());

        verify(showService).updateShow(
                showId,
                req.getStartTime(),
                req.getEndTime()
        );
    }

    @Test
    void updateShow_shouldPropagateException_whenServiceFails() {

        UUID showId = UUID.randomUUID();

        UpdateShowRequestDto req = new UpdateShowRequestDto();

        when(showService.updateShow(any(), any(), any()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.updateShow(showId, req));
    }

    // ================= DELETE =================

    @Test
    void deleteShow_shouldCallService_andReturnNoContent() {

        UUID showId = UUID.randomUUID();

        ResponseEntity<Void> response =
                controller.deleteShow(showId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(showService).deleteShow(showId);
    }

    @Test
    void deleteShow_shouldPropagateException_whenServiceFails() {

        UUID showId = UUID.randomUUID();

        doThrow(new RuntimeException())
                .when(showService).deleteShow(showId);

        assertThrows(RuntimeException.class,
                () -> controller.deleteShow(showId));

        verify(showService).deleteShow(showId);
    }
}