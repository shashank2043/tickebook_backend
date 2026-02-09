package org.team11.tickebook.theatreservice.service;

import org.springframework.security.core.Authentication;
import org.team11.tickebook.theatreservice.dto.request.CreateSeatRequest;
import org.team11.tickebook.theatreservice.dto.response.SeatDto;
import org.team11.tickebook.theatreservice.model.Seat;

import java.util.List;

public interface SeatService {
    Seat create(CreateSeatRequest request, Authentication authentication);

    List<Seat> getByScreen(Long screenId, Authentication authentication);

    void deactivate(Long seatId, Authentication authentication);

    List<SeatDto> getByScreenAsDto(Long screenId, Authentication authentication);
}
