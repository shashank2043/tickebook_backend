package org.team11.tickebook.consumerservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team11.tickebook.consumerservice.dto.request.BookingRequestDto;
import org.team11.tickebook.consumerservice.dto.response.BookingResponseDto;
import org.team11.tickebook.consumerservice.model.Booking;
import org.team11.tickebook.consumerservice.model.BookingStatus;
import org.team11.tickebook.consumerservice.model.PaymentStatus;
import org.team11.tickebook.consumerservice.repository.BookingRepository;
import org.team11.tickebook.consumerservice.service.BookingService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public BookingResponseDto createBooking(BookingRequestDto request) {

        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setShow(request.getShow());
        booking.setBookingNumber(generateBookingNumber());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setTotalAmount(request.getTotalAmount());
        booking.setBookedAt(LocalDateTime.now());
        booking.setExpiresAt(request.getExpiresAt());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    @Override
    public BookingResponseDto getBookingById(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return mapToResponse(booking);
    }

    private String generateBookingNumber() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private BookingResponseDto mapToResponse(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .show(booking.getShow())
                .bookingNumber(booking.getBookingNumber())
                .bookingStatus(booking.getBookingStatus())
                .paymentStatus(booking.getPaymentStatus())
                .totalAmount(booking.getTotalAmount())
                .bookedAt(booking.getBookedAt())
                .expiresAt(booking.getExpiresAt())
                .build();
    }
}

