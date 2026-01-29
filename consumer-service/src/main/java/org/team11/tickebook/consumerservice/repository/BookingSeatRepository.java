package org.team11.tickebook.consumerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.consumerservice.model.Booking;
import org.team11.tickebook.consumerservice.model.BookingSeat;

import java.util.List;
import java.util.UUID;
@Repository
public interface BookingSeatRepository  extends JpaRepository<BookingSeat, UUID> {
    List<BookingSeat> findByBooking_Id(UUID bookingId);

    List<BookingSeat> findByShowId(UUID showId);
}
