package org.team11.tickebook.show_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.show_service.client.TheatreClient;
import org.team11.tickebook.show_service.dto.SeatDto;
import org.team11.tickebook.show_service.exception.ShowCannotBeUpdatedException;
import org.team11.tickebook.show_service.model.SeatType;
import org.team11.tickebook.show_service.model.Show;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.repository.ShowRepository;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;
import org.team11.tickebook.show_service.service.exception.ShowNotFoundException;
import org.team11.tickebook.show_service.service.impl.ShowServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowServiceImplTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private TheatreClient theatreClient;

    @InjectMocks
    private ShowServiceImpl service;

    // ================= CREATE SHOW =================

    @Test
    void createShow_shouldCreateShow_andGenerateSeats() {

        UUID movieId = UUID.randomUUID();
        Long screenId = 1L;

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        Map<SeatType, BigDecimal> priceMap = Map.of(
                SeatType.SILVER, BigDecimal.valueOf(100),
                SeatType.GOLD, BigDecimal.valueOf(200)
        );

        Show savedShow = new Show();
        savedShow.setId(UUID.randomUUID());

        when(showRepository.save(any(Show.class)))
                .thenAnswer(invocation -> {
                    Show s = invocation.getArgument(0);
                    s.setId(UUID.randomUUID()); // simulate DB generated ID
                    return s;
                });

        SeatDto seatDto = new SeatDto(1L, "A", 1, SeatType.SILVER, 0, 0, true);

        when(theatreClient.getSeatsByScreen(screenId))
                .thenReturn(List.of(seatDto));

        Show result = service.createShow(movieId, screenId, start, end, priceMap);

        assertNotNull(result);
        assertEquals(movieId, result.getMovieId());

        verify(showRepository).save(any(Show.class));
        verify(theatreClient).getSeatsByScreen(screenId);
        verify(showSeatRepository).saveAll(anyList());
    }

    // ================= GET SEATS =================

    @Test
    void getSeats_shouldReturnSeats() {

        UUID showId = UUID.randomUUID();
        List<ShowSeat> seats = List.of(new ShowSeat());

        when(showSeatRepository.findByShowId(showId)).thenReturn(seats);

        List<ShowSeat> result = service.getSeats(showId);

        assertEquals(seats, result);
        verify(showSeatRepository).findByShowId(showId);
    }

    // ================= UPDATE SHOW =================

    @Test
    void updateShow_shouldUpdate_whenNotStarted() {

        UUID showId = UUID.randomUUID();

        Show show = new Show();
        show.setId(showId);
        show.setStartTime(LocalDateTime.now().plusHours(2)); // future

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(showRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        LocalDateTime newStart = LocalDateTime.now().plusHours(3);
        LocalDateTime newEnd = newStart.plusHours(2);

        Show result = service.updateShow(showId, newStart, newEnd);

        assertEquals(newStart, result.getStartTime());
        assertEquals(newEnd, result.getEndTime());

        verify(showRepository).save(show);
    }

    @Test
    void updateShow_shouldThrowException_whenStarted() {

        UUID showId = UUID.randomUUID();

        Show show = new Show();
        show.setStartTime(LocalDateTime.now().minusHours(1)); // already started

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));

        assertThrows(ShowCannotBeUpdatedException.class,
                () -> service.updateShow(showId,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)));
    }

    @Test
    void updateShow_shouldThrowException_whenNotFound() {

        UUID showId = UUID.randomUUID();

        when(showRepository.findById(showId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateShow(showId,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)));
    }

    // ================= DELETE SHOW =================

    @Test
    void deleteShow_shouldDeactivateShow() {

        UUID showId = UUID.randomUUID();

        Show show = new Show();
        show.setId(showId);
        show.setIsActive(true);

        when(showRepository.findById(showId)).thenReturn(Optional.of(show));

        service.deleteShow(showId);

        assertFalse(show.getIsActive());
        verify(showRepository).save(show);
    }

    @Test
    void deleteShow_shouldThrowException_whenNotFound() {

        UUID showId = UUID.randomUUID();

        when(showRepository.findById(showId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.deleteShow(showId));
    }

    // ================= GET ALL =================

    @Test
    void getAllShows_shouldReturnList() {

        List<Show> list = List.of(new Show(), new Show());

        when(showRepository.findAll()).thenReturn(list);

        List<Show> result = service.getAllShows();

        assertEquals(2, result.size());
        verify(showRepository).findAll();
    }

    // ================= GET ONE =================

    @Test
    void getShow_shouldReturnShow_whenExists() {

        UUID id = UUID.randomUUID();
        Show show = new Show();

        when(showRepository.findById(id)).thenReturn(Optional.of(show));

        Show result = service.getShow(id);

        assertEquals(show, result);
    }

    @Test
    void getShow_shouldThrowException_whenNotFound() {

        UUID id = UUID.randomUUID();

        when(showRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ShowNotFoundException.class,
                () -> service.getShow(id));
    }
}
