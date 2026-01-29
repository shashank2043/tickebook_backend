package org.team11.tickebook.consumerservice.service;

import org.team11.tickebook.consumerservice.model.BookingSeat;

import java.util.List;
import java.util.UUID;

public interface BookingSeatService {

    BookingSeat createBookingSeat(BookingSeat bookingSeat);

    BookingSeat getBookingSeatById(UUID id);

    List<BookingSeat> getSeatsByBookingId(UUID bookingId);

    List<BookingSeat> getSeatsByShowId(UUID showId);

    BookingSeat updateSeatStatus(UUID id, String status);

    void deleteBookingSeat(UUID id);
}
