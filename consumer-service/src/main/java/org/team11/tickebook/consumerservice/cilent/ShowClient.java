package org.team11.tickebook.consumerservice.cilent;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.team11.tickebook.consumerservice.dto.Show;

import java.util.List;

@FeignClient(name = "SHOW-SERVICE",url = "/api/shows")
public interface ShowClient {
    @GetMapping
    public List<Show> getAllShows();
}
