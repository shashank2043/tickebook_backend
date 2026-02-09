package org.team11.tickebook.show_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.show_service.client.TheatreClient;
import org.team11.tickebook.show_service.dto.SeatDto;
import org.team11.tickebook.show_service.exception.ShowCannotBeUpdatedException;
import org.team11.tickebook.show_service.model.SeatType;
import org.team11.tickebook.show_service.model.Show;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.model.ShowSeatStatus;
import org.team11.tickebook.show_service.repository.ShowRepository;
import org.team11.tickebook.show_service.repository.ShowSeatRepository;
import org.team11.tickebook.show_service.service.ShowService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final TheatreClient theatreClient;

    @Override
    @Transactional
    public Show createShow(UUID movieId, Long screenId, LocalDateTime start, LocalDateTime end, Map<SeatType, BigDecimal> priceMap) {
        Show show = new Show();
        show.setMovieId(movieId);
        show.setScreenId(screenId);
        show.setStartTime(start);
        show.setEndTime(end);
        show.setActive(true);
        Show savedShow = showRepository.save(show);
        List<SeatDto> seats = theatreClient.getSeatsByScreen(screenId);
        List<ShowSeat> showSeats = seats.stream().map(seat -> {
            ShowSeat ss = new ShowSeat();
            ss.setShowId(savedShow.getId());
            ss.setSeatId(seat.getId());
            ss.setStatus(ShowSeatStatus.AVAILABLE);
            ss.setPrice(priceMap.get(seat.getSeatType()));
            return ss;
        }).toList();
        showSeatRepository.saveAll(showSeats);
        return savedShow;
    }

    @Override
    public List<ShowSeat> getSeats(UUID showId) {
        return showSeatRepository.findByShowId(showId);
    }

    @Override
    public Show updateShow(UUID showId, LocalDateTime start, LocalDateTime end) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // Optional Rule – prevent update after start
        if (show.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ShowCannotBeUpdatedException("Cannot update started show");
        }

        show.setStartTime(start);
        show.setEndTime(end);

        return showRepository.save(show);
    }

    @Override
    public void deleteShow(UUID showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        show.setActive(false);

        showRepository.save(show);
    }

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

}
