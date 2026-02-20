package org.team11.tickebook.show_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team11.tickebook.show_service.model.ShowSeat;
import org.team11.tickebook.show_service.service.ShowSeatService;

@RestController
@RequestMapping("/internal/seats")
@RequiredArgsConstructor
public class InternalController {

    private final ShowSeatService seatService;

    @PostMapping("/lock")
    public ShowSeat lockSeat(@RequestBody ShowSeat seat) {
        return seatService.lockSeat(seat);
    }

    @PostMapping("/confirm")
    public ShowSeat confirmSeat(@RequestBody ShowSeat seat) {
        return seatService.confirmSeat(seat);
    }

    @PostMapping("/release")
    public ShowSeat releaseSeat(@RequestBody ShowSeat seat) {
        return seatService.releaseSeat(seat);
    }
}

