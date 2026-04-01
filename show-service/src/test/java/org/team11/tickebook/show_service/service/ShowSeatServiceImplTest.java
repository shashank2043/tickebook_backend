package org.team11.tickebook.show_service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.model.ShowSeatStatus;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowSeatServiceImplTest {

    @Mock
    private ShowSeatRepository repository;

    @InjectMocks
    private ShowSeatServiceImpl service;

    // ================= LOCK SEAT =================

    @Test
    void lockSeat_shouldLockSeat_whenAvailable() {

        UUID showId = UUID.randomUUID();
        Long seatId = 1L;

        ShowSeat existing = new ShowSeat();
        existing.setShowId(showId);
        existing.setSeatId(seatId);
        existing.setStatus(ShowSeatStatus.AVAILABLE);

        ShowSeat request = new ShowSeat();
        request.setShowId(showId);
        request.setSeatId(seatId);
        request.setLockedAt(LocalDateTime.now());
        request.setLockedByUserId(UUID.randomUUID());

        when(repository.findByShowIdAndSeatId(showId, seatId))
                .thenReturn(Optional.of(existing));

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        ShowSeat result = service.lockSeat(request);

        assertEquals(ShowSeatStatus.LOCKED, result.getStatus());
        assertNotNull(result.getLockedAt());
        assertNotNull(result.getLockedByUserId());

        verify(repository).save(existing);
    }

    @Test
    void lockSeat_shouldThrowException_whenSeatNotFound() {

        ShowSeat request = new ShowSeat();
        request.setShowId(UUID.randomUUID());
        request.setSeatId(1L);

        when(repository.findByShowIdAndSeatId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.lockSeat(request));
    }

    @Test
    void lockSeat_shouldThrowException_whenSeatNotAvailable() {

        UUID showId = UUID.randomUUID();
        Long seatId = 1L;

        ShowSeat existing = new ShowSeat();
        existing.setShowId(showId);
        existing.setSeatId(seatId);
        existing.setStatus(ShowSeatStatus.BOOKED);

        ShowSeat request = new ShowSeat();
        request.setShowId(showId);
        request.setSeatId(seatId);

        when(repository.findByShowIdAndSeatId(showId, seatId))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> service.lockSeat(request));
    }

    // ================= CONFIRM SEAT =================

    @Test
    void confirmSeat_shouldBookSeat_whenLocked() {

        UUID showId = UUID.randomUUID();
        Long seatId = 1L;

        ShowSeat existing = new ShowSeat();
        existing.setShowId(showId);
        existing.setSeatId(seatId);
        existing.setStatus(ShowSeatStatus.LOCKED);

        ShowSeat request = new ShowSeat();
        request.setShowId(showId);
        request.setSeatId(seatId);

        when(repository.findByShowIdAndSeatId(showId, seatId))
                .thenReturn(Optional.of(existing));

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        ShowSeat result = service.confirmSeat(request);

        assertEquals(ShowSeatStatus.BOOKED, result.getStatus());
        assertNull(result.getLockedAt());
        assertNull(result.getLockedByUserId());

        verify(repository).save(existing);
    }

    @Test
    void confirmSeat_shouldThrowException_whenSeatNotFound() {

        ShowSeat request = new ShowSeat();
        request.setShowId(UUID.randomUUID());
        request.setSeatId(1L);

        when(repository.findByShowIdAndSeatId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.confirmSeat(request));
    }

    @Test
    void confirmSeat_shouldThrowException_whenSeatNotLocked() {

        UUID showId = UUID.randomUUID();
        Long seatId = 1L;

        ShowSeat existing = new ShowSeat();
        existing.setShowId(showId);
        existing.setSeatId(seatId);
        existing.setStatus(ShowSeatStatus.AVAILABLE);

        ShowSeat request = new ShowSeat();
        request.setShowId(showId);
        request.setSeatId(seatId);

        when(repository.findByShowIdAndSeatId(showId, seatId))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> service.confirmSeat(request));
    }

    // ================= RELEASE SEAT =================

    @Test
    void releaseSeat_shouldMakeSeatAvailable() {

        UUID showId = UUID.randomUUID();
        Long seatId = 1L;

        ShowSeat existing = new ShowSeat();
        existing.setShowId(showId);
        existing.setSeatId(seatId);
        existing.setStatus(ShowSeatStatus.LOCKED);

        ShowSeat request = new ShowSeat();
        request.setShowId(showId);
        request.setSeatId(seatId);

        when(repository.findByShowIdAndSeatId(showId, seatId))
                .thenReturn(Optional.of(existing));

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        ShowSeat result = service.releaseSeat(request);

        assertEquals(ShowSeatStatus.AVAILABLE, result.getStatus());
        assertNull(result.getLockedAt());
        assertNull(result.getLockedByUserId());

        verify(repository).save(existing);
    }

    @Test
    void releaseSeat_shouldThrowException_whenSeatNotFound() {

        ShowSeat request = new ShowSeat();
        request.setShowId(UUID.randomUUID());
        request.setSeatId(1L);

        when(repository.findByShowIdAndSeatId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.releaseSeat(request));
    }
}