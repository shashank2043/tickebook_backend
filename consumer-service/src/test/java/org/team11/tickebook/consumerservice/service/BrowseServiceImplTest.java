package org.team11.tickebook.consumerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.Show;
import org.team11.tickebook.consumerservice.service.impl.BrowseServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrowseServiceImplTest {

    @Mock
    private ShowClient showClient;

    @InjectMocks
    private BrowseServiceImpl service;

    // ================= FETCH ALL SHOWS =================

    @Test
    void fetchAllShows_shouldReturnShowList_whenClientReturnsData() {

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

        List<Show> result = service.fetchAllShows();

        assertEquals(shows, result);
        verify(showClient).getAllShows();
    }

    @Test
    void fetchAllShows_shouldReturnEmptyList_whenNoShows() {

        when(showClient.getAllShows()).thenReturn(List.of());

        List<Show> result = service.fetchAllShows();

        assertTrue(result.isEmpty());
        verify(showClient).getAllShows();
    }

    @Test
    void fetchAllShows_shouldPropagateException_whenClientFails() {

        when(showClient.getAllShows())
                .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class,
                () -> service.fetchAllShows());

        verify(showClient).getAllShows();
    }
}