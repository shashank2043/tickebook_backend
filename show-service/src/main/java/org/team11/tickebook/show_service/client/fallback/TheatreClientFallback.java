package org.team11.tickebook.show_service.client.fallback;

import org.springframework.stereotype.Component;
import org.team11.tickebook.show_service.client.TheatreClient;
import org.team11.tickebook.show_service.dto.SeatDto;

import java.util.List;

@Component
public class TheatreClientFallback implements TheatreClient {

    @Override
    public List<SeatDto> getSeatsByScreen(Long screenId) {
        return List.of();
    }
}
