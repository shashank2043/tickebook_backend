package org.team11.tickebook.consumerservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team11.tickebook.consumerservice.client.PaymentClient;
import org.team11.tickebook.consumerservice.client.ShowClient;
import org.team11.tickebook.consumerservice.dto.PaymentRequest;
import org.team11.tickebook.consumerservice.dto.PaymentResponse;
import org.team11.tickebook.consumerservice.dto.ShowSeat;
import org.team11.tickebook.consumerservice.dto.ShowSeatStatus;
import org.team11.tickebook.consumerservice.model.*;
import org.team11.tickebook.consumerservice.repository.BookingRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingPaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentClient paymentClient;
    private final TicketService ticketService;
    private final ShowClient showClient;
    @Transactional
    public Ticket payForBooking(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking not in payable state");
        }

        // Call payment service
        PaymentRequest request = new PaymentRequest(
                booking.getId(),
                booking.getTotalAmount()
        );

        PaymentResponse response = paymentClient.processPayment(request);

        if (!response.success()) {
            booking.setPaymentStatus(PaymentStatus.FAILED);
            booking.setBookingStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            throw new RuntimeException("Payment failed");
        }

        // Payment success
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.getSeats().forEach(bookingSeat -> bookingSeat.setStatus(BookingSeatStatus.BOOKED));
        bookingRepository.save(booking);

        List<ShowSeat> updatedSeats = booking.getSeats()
                .stream()
                .map(bs -> {
                    ShowSeat seat = new ShowSeat();
                    seat.setSeatId(bs.getSeatId());
                    seat.setShowId(bs.getShowId());
                    seat.setStatus(ShowSeatStatus.BOOKED);
                    seat.setLockedAt(null);
                    seat.setLockedByUserId(null);
                    return seat;
                })
                .toList();

        //book seat
        try {
            updatedSeats.stream().forEach(showClient::confirmSeat);
        } catch (FeignException e) {
            throw new RuntimeException("Seat confirmation failed: " + e.contentUTF8());
        }
        // Generate ticket
        return ticketService.generateTicket(Ticket.builder().booking(booking).build());
    }
}
