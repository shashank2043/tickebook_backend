package org.team11.tickebook.consumerservice.client.fallback;

import org.springframework.stereotype.Component;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.Show;
import org.team11.tickebook.consumerservice.dto.ShowSeat;

import java.util.List;
import java.util.UUID;

@Component
public class ShowClientFallback implements ShowClient {

    @Override
    public List<Show> getAllShows() {
        return List.of();
    }

    @Override
    public Show getShow(UUID uuid) {
        return null;
    }

    @Override
    public List<ShowSeat> getSeats(UUID showId) {
        return List.of();
    }

    @Override
    public ShowSeat lockSeat(ShowSeat seat) {
        return null;
    }

    @Override
    public ShowSeat confirmSeat(ShowSeat seat) {
        return null;
    }

    @Override
    public ShowSeat releaseSeat(ShowSeat seat) {
        return null;
    }
}
