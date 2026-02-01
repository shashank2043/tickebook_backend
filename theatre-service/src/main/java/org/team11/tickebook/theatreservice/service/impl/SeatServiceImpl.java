package org.team11.tickebook.theatreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.theatreservice.model.Seat;
import org.team11.tickebook.theatreservice.repository.SeatRepository;
import org.team11.tickebook.theatreservice.service.SeatService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository repo;

    @Override
    public Seat create(Seat seat) {
        seat.setActive(true);
        return repo.save(seat);
    }

    @Override
    public List<Seat> getByScreen(Long screenId) {
        return repo.findByScreenId(screenId);
    }
}
