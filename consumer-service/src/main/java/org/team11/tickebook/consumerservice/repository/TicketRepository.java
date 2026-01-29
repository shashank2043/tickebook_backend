package org.team11.tickebook.consumerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team11.tickebook.consumerservice.model.Ticket;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Optional<Ticket> findByBooking_Id(UUID bookingId);
}
