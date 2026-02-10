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
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {
    private final ShowSeatService seatService;
    @PostMapping("/bookseat")
    public ResponseEntity<?> updateSeat(@RequestBody ShowSeat showSeat){
        return ResponseEntity.ok(seatService.bookSeat(showSeat));
    }
}
