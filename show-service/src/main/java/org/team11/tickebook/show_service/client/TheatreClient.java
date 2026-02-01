package org.team11.tickebook.show_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.team11.tickebook.show_service.dto.SeatDto;

import java.util.List;
import java.util.UUID;

@FeignClient("THEATRE-SERVICE")
public interface TheatreClient {
    @GetMapping("/api/seats/screen/{screenId}")
    public List<SeatDto> getSeatsByScreen(@PathVariable Long screenId);
}
