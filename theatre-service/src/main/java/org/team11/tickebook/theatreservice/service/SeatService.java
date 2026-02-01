package org.team11.tickebook.theatreservice.service;

import org.team11.tickebook.theatreservice.model.Seat;

import java.util.List;

public interface SeatService {
    Seat create(Seat seat);
    List<Seat> getByScreen(Long screenId);
}
