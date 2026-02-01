package org.team11.tickebook.show_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team11.tickebook.show_service.model.Show;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.model.ShowSeatStatus;
import org.team11.tickebook.show_service.repository.ShowRepository;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;
import org.team11.tickebook.show_service.service.ShowService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;


    @Override
    public Show createShow(UUID movieId, UUID screenId, LocalDateTime start, LocalDateTime end) {
        Show show = new Show();
        show.setMovieId(movieId);
        show.setScreenId(screenId);
        show.setStartTime(start);
        show.setEndTime(end);
        show.setActive(true);
        Show savedShow = showRepository.save(show);
        List<SeatDTO> seats = theatreClient.getSeatsByScreen(screenId);
        List<ShowSeat> showSeats = seats.stream().map(seat -> {
            ShowSeat ss = new ShowSeat();
            ss.setShowId(savedShow.getId());
            ss.setSeatId(seat.getId());
            ss.setStatus(ShowSeatStatus.AVAILABLE);
            ss.setPrice(seat.getPrice());
            return ss;
        }).toList();
    }

    @Override
    public List<ShowSeat> getSeats(UUID showId) {
        return List.of();
    }

    @Override
    public Show updateShow(UUID showId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public void deleteShow(UUID showId) {

    }
}
