package org.team11.tickebook.show_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.show_service.exception.ShowSeatDoesNotExistException;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.model.ShowSeatStatus;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;
import org.team11.tickebook.show_service.service.ShowSeatService;
@Service
@RequiredArgsConstructor
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowSeatRepository repository;

    @Override
    @Transactional
    public ShowSeat lockSeat(ShowSeat dto) {

        ShowSeat seat = repository
                .findByShowIdAndSeatId(dto.getShowId(), dto.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() != ShowSeatStatus.AVAILABLE) {
            throw new IllegalStateException("Seat not available");
        }

        seat.setStatus(ShowSeatStatus.LOCKED);
        seat.setLockedAt(dto.getLockedAt());
        seat.setLockedByUserId(dto.getLockedByUserId());

        return repository.save(seat);
    }

    @Override
    @Transactional
    public ShowSeat confirmSeat(ShowSeat dto) {

        ShowSeat seat = repository
                .findByShowIdAndSeatId(dto.getShowId(), dto.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() != ShowSeatStatus.LOCKED) {
            throw new IllegalStateException("Seat not locked");
        }

        seat.setStatus(ShowSeatStatus.BOOKED);
        seat.setLockedAt(null);
        seat.setLockedByUserId(null);

        return repository.save(seat);
    }

    @Override
    @Transactional
    public ShowSeat releaseSeat(ShowSeat dto) {

        ShowSeat seat = repository
                .findByShowIdAndSeatId(dto.getShowId(), dto.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setStatus(ShowSeatStatus.AVAILABLE);
        seat.setLockedAt(null);
        seat.setLockedByUserId(null);

        return repository.save(seat);
    }
}
