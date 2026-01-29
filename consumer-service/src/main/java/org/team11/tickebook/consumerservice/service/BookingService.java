package org.team11.tickebook.consumerservice.service;

import org.team11.tickebook.consumerservice.dto.request.BookingRequestDto;
import org.team11.tickebook.consumerservice.dto.response.BookingResponseDto;

import java.util.UUID;

    public interface BookingService {

        BookingResponseDto createBooking(BookingRequestDto request);

        BookingResponseDto getBookingById(UUID bookingId);
    }


