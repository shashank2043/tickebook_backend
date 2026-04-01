package org.team11.tickebook.consumerservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.Show;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrowseControllerTest {

    @Mock
    private ShowClient showClient;

    @InjectMocks
    private BrowseController controller;

    // ================= GET SHOWS =================

    @Test
    void getShows_shouldReturnShowList_whenClientReturnsData() {

        Show show = new Show(
                UUID.randomUUID(),
                UUID.randomUUID(),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                true
        );

        List<Show> shows = List.of(show);

        when(showClient.getAllShows()).thenReturn(shows);

        ResponseEntity<?> response = controller.getShows();

        // ✅ Status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // ✅ Body
        assertEquals(shows, response.getBody());

        verify(showClient).getAllShows();
    }

    @Test
    void getShows_shouldReturnEmptyList_whenNoShowsAvailable() {

        when(showClient.getAllShows()).thenReturn(List.of());

        ResponseEntity<?> response = controller.getShows();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());

        verify(showClient).getAllShows();
    }

    @Test
    void getShows_shouldPropagateException_whenClientFails() {

        when(showClient.getAllShows())
                .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class,
                () -> controller.getShows());

        verify(showClient).getAllShows();
    }
}