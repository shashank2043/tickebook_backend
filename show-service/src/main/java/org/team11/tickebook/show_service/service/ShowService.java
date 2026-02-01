package org.team11.tickebook.show_service.service;

import org.team11.tickebook.show_service.model.SeatType;
import org.team11.tickebook.show_service.model.Show;
import org.team11.tickebook.show_service.model.ShowSeat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShowService {
    Show createShow(UUID movieId, Long screenId, LocalDateTime start, LocalDateTime end, Map<SeatType, BigDecimal> priceMap);
    List<ShowSeat> getSeats(UUID showId);
    Show updateShow(UUID showId,
                    LocalDateTime start, LocalDateTime end);

    void deleteShow(UUID showId);
}
