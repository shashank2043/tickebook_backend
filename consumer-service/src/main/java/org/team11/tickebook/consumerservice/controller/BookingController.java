package org.team11.tickebook.consumerservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.consumerservice.dto.request.BookingRequestDto;
import org.team11.tickebook.consumerservice.dto.response.BookingResponseDto;
import org.team11.tickebook.consumerservice.service.BookingService;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto request, HttpServletRequest servletRequest) {
        UUID userId = UUID.fromString(servletRequest.getHeader("X-User-Id"));
        request.setUserId(userId);
        BookingResponseDto response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(
            @PathVariable UUID id) {

        BookingResponseDto response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }
}

