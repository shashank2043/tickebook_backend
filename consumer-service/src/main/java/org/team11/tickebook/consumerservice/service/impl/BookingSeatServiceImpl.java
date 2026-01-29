package org.team11.tickebook.consumerservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team11.tickebook.consumerservice.model.BookingSeat;
import org.team11.tickebook.consumerservice.model.BookingSeatStatus;
import org.team11.tickebook.consumerservice.repository.BookingSeatRepository;
import org.team11.tickebook.consumerservice.service.BookingSeatService;

import java.util.List;
import java.util.UUID;

@Service

public class BookingSeatServiceImpl implements BookingSeatService {
    @Autowired
    private  BookingSeatRepository bookingSeatRepository;

    @Override
    public BookingSeat createBookingSeat(BookingSeat bookingSeat) {
        bookingSeat.setStatus(BookingSeatStatus.BOOKED);
        return bookingSeatRepository.save(bookingSeat);
    }

    @Override
    public BookingSeat getBookingSeatById(UUID id) {
        return bookingSeatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookingSeat not found"));
    }

    @Override
    public List<BookingSeat> getSeatsByBookingId(UUID bookingId) {
        return bookingSeatRepository.findByBooking_Id(bookingId);
    }

    @Override
    public List<BookingSeat> getSeatsByShowId(UUID showId) {
        return bookingSeatRepository.findByShowId(showId);
    }

    @Override
    public BookingSeat updateSeatStatus(UUID id, String status) {
        BookingSeat seat = getBookingSeatById(id);
        seat.setStatus(BookingSeatStatus.valueOf(status));
        return bookingSeatRepository.save(seat);
    }

    @Override
    public void deleteBookingSeat(UUID id) {
        bookingSeatRepository.deleteById(id);
    }
}

