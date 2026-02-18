package org.team11.tickebook.consumerservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.consumerservice.cilent.ShowClient;
import org.team11.tickebook.consumerservice.dto.Show;
import org.team11.tickebook.consumerservice.dto.ShowSeat;
import org.team11.tickebook.consumerservice.dto.ShowSeatStatus;
import org.team11.tickebook.consumerservice.dto.request.BookingRequestDto;
import org.team11.tickebook.consumerservice.dto.response.BookingResponseDto;
import org.team11.tickebook.consumerservice.model.*;
import org.team11.tickebook.consumerservice.repository.BookingRepository;
import org.team11.tickebook.consumerservice.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowClient showClient;

    //    @Override
//    @Transactional
//    public BookingResponseDto createBooking(BookingRequestDto request) {
//        if (request.getSeats() == null || request.getSeats().isEmpty()) {
//            throw new RuntimeException("No seats selected");
//        }
//
//        // ---------- 1. Create Booking ----------
//        Booking booking = new Booking();
//        booking.setUserId(request.getUserId());
//        booking.setBookingNumber(generateBookingNumber());
//        booking.setBookingStatus(BookingStatus.PENDING);
//        booking.setPaymentStatus(PaymentStatus.PENDING);
//        booking.setBookedAt(LocalDateTime.now());
//        booking.setCreatedAt(LocalDateTime.now());
//        booking.setUpdatedAt(LocalDateTime.now());
//        booking.setExpiresAt(LocalDateTime.now().plusMinutes(10));
//
//        List<BookingSeat> bookingSeats = new ArrayList<>();
//        BigDecimal totalAmount = BigDecimal.ZERO;
//
//        // ---------- 2. Process Seats ----------
//        for (ShowSeat seatDto : request.getSeats()) {
//
//            // lock seat via show-service
//            seatDto.setStatus(ShowSeatStatus.LOCKED);
//            seatDto.setLockedAt(LocalDateTime.now());
//            seatDto.setLockedByUserId(request.getUserId());
//            ShowSeat lockedSeat = showClient.bookSeat(seatDto);
//
//            if (lockedSeat == null) {
//                throw new RuntimeException("Seat locking failed: " + seatDto.getSeatId());
//            }
//
//            // create BookingSeat entity
//            BookingSeat bookingSeat = new BookingSeat();
//            bookingSeat.setBooking(booking);
//            bookingSeat.setSeatId(lockedSeat.getSeatId());
//            bookingSeat.setShowId(lockedSeat.getShowId());
//            bookingSeat.setSeatPrice(lockedSeat.getPrice());
//            bookingSeat.setStatus(BookingSeatStatus.LOCKED);
//
//            bookingSeats.add(bookingSeat);
//            totalAmount = totalAmount.add(lockedSeat.getPrice());
//        }
//
//        // ---------- 3. Finalize Booking ----------
//        booking.setTotalAmount(totalAmount);
//        booking.setSeats(bookingSeats);
//        booking.setShow(bookingSeats.get(0).getShowId()); // assuming same show
//
//        // ---------- 4. Save ----------
//        Booking savedBooking = bookingRepository.save(booking);
//
//        return mapToResponse(savedBooking);
//    }
    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request) {

        if (request.getSeatId() == null || request.getSeatId().isEmpty()) {
            throw new RuntimeException("No seats selected");
        }

        // ---------- 1. Create Booking ----------
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setBookingNumber(generateBookingNumber());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setBookedAt(LocalDateTime.now());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        List<BookingSeat> bookingSeats = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // ---------- 2. Fetch Seats From Show Service ----------
        List<ShowSeat> allSeats = showClient.getSeats(request.getShowId());

        // filter only requested seatIds
        List<ShowSeat> requestedSeats = allSeats.stream()
                .filter(s -> request.getSeatId().contains(s.getSeatId()))
                .toList();

        if (requestedSeats.size() != request.getSeatId().size()) {
            throw new RuntimeException("Some seats not found");
        }

        // ---------- 3. Validate + Lock ----------
        for (ShowSeat seat : requestedSeats) {

            if (seat.getStatus() != ShowSeatStatus.AVAILABLE) {
                throw new RuntimeException(
                        "Seat already booked/locked: " + seat.getSeatId()
                );
            }

            // lock seat
            seat.setStatus(ShowSeatStatus.LOCKED);
            seat.setLockedAt(LocalDateTime.now());
            seat.setLockedByUserId(request.getUserId());

            ShowSeat lockedSeat = showClient.bookSeat(seat);

            if (lockedSeat == null) {
                throw new RuntimeException("Seat locking failed: " + seat.getSeatId());
            }

            // ---------- Create BookingSeat ----------
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(booking);
            bookingSeat.setSeatId(lockedSeat.getSeatId());
            bookingSeat.setShowId(lockedSeat.getShowId());
            bookingSeat.setSeatPrice(lockedSeat.getPrice());
            bookingSeat.setStatus(BookingSeatStatus.LOCKED);

            bookingSeats.add(bookingSeat);
            totalAmount = totalAmount.add(lockedSeat.getPrice());
        }

        // ---------- 4. Finalize ----------
        booking.setTotalAmount(totalAmount);
        booking.setSeats(bookingSeats);
        booking.setShow(request.getShowId());

        // ---------- 5. Save ----------
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

