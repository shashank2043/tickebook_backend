//package org.team11.tickebook.consumerservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.team11.tickebook.consumerservice.model.Booking;
//import org.team11.tickebook.consumerservice.model.BookingStatus;
//import org.team11.tickebook.consumerservice.model.PaymentStatus;
//import org.team11.tickebook.consumerservice.model.Ticket;
//import org.team11.tickebook.consumerservice.repository.BookingRepository;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class InternalBookingService {
//
//    private final BookingRepository bookingRepository;
//    private final TicketService ticketService;
//
//    @Transactional
//    public Ticket confirmBooking(UUID bookingId) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        // idempotency check
//        if (booking.getPaymentStatus() == PaymentStatus.PAID) {
//            return null;
//        }
//
//        booking.setPaymentStatus(PaymentStatus.PAID);
//        booking.setBookingStatus(BookingStatus.CONFIRMED);
//        Ticket ticket = new Ticket();
//        ticket.setBooking(booking);
//        bookingRepository.save(booking);
//        return ticketService.generateTicket(ticket);
//    }
//
//    @Transactional
//    public void failBooking(UUID bookingId) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        // idempotency check
//        if (booking.getPaymentStatus() == PaymentStatus.FAILED) {
//            return;
//        }
//
//        booking.setPaymentStatus(PaymentStatus.FAILED);
//        booking.setBookingStatus(BookingStatus.CANCELLED);
//
//        bookingRepository.save(booking);
//    }
//}
