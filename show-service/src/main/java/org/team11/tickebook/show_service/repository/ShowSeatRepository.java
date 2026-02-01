package org.team11.tickebook.show_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team11.tickebook.show_service.model.ShowSeat;

import java.util.List;
import java.util.UUID;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, UUID> {
    List<ShowSeat> findByShowId(UUID showId);
}