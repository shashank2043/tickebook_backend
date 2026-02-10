package org.team11.tickebook.consumerservice.cilent;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.team11.tickebook.consumerservice.dto.Show;
import org.team11.tickebook.consumerservice.dto.ShowSeat;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "SHOW-SERVICE")
public interface ShowClient {
    @GetMapping
    public List<Show> getAllShows();
    @GetMapping("/api/shows/{showid}")
    public Show getShow(@PathVariable UUID uuid);
    @GetMapping("/api/shows/{showId}/seats")
    public List<ShowSeat> getSeats(@PathVariable UUID showId);
    @PostMapping("/internal/bookseat")
    ShowSeat bookSeat(@RequestBody ShowSeat showSeat);
}
